package com.cineevent.userservice.cache;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.services.UserService;
import com.google.gson.Gson;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
@Log4j2
public class UserCache {
	
	private static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
	private static final int TTL_TIME = 5*60;

	private JedisPool jedisPool;
	
	private Gson gson = new Gson();
	
	@Value("${redis.url}")
	private String redisEndpoint;
	
	@Value("${redis.port}")
	private int redisPort;
	
	@Value("${redis.maxConnection}")
	private int redisMaxConnection;
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void init() {
		this.jedisPool = initializeJedisPool();

	}
	
	private JedisPool initializeJedisPool() {
		try {
			log.info("initializing with redisEndpoint={}, redisPort={}, redisMaxConnection={}", redisEndpoint, redisPort, redisMaxConnection);
			GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<Jedis>();
			poolConfig.setMaxTotal(redisMaxConnection);
			JedisPool jedisPool = new JedisPool(poolConfig, redisEndpoint, redisPort,
					DEFAULT_CONNECTION_TIMEOUT);
			return jedisPool;
		} catch (Exception e) {
			log.error("Terminating, Error occurred while initializing redis");
			throw new RuntimeException(
					"Terminating, Error occurred while initializing redis");
		}
	}
	
	public void setUserInCache(UserResponseDTO userResponseDTO) {
		log.info("setUserInCache for userId {}", userResponseDTO.getId());
		Jedis jedis = jedisPool.getResource();
		String userResponseString = gson.toJson(userResponseDTO);
		jedis.set(getUserKey(userResponseDTO.getId()), userResponseString);
		jedis.expire(getUserKey(userResponseDTO.getId()), TTL_TIME);
	}
	
	public UserResponseDTO getUser(int userId) {
		UserResponseDTO userResponseDTO = getUserFromCache(userId);
		if(userResponseDTO == null) {
			log.info("getUser from db for userId {}", userId);
			userResponseDTO = userService.getUserById(userId);
		}
		setUserInCache(userResponseDTO);
		return userResponseDTO;
	}
	
	public UserResponseDTO getUserFromCache(int userId) {
		log.info("getUserFromCache for userId {}", userId);
		Jedis jedis = jedisPool.getResource();
		String userInCache = jedis.get(getUserKey(userId));
		if(!StringUtils.isEmpty(userInCache)) {
			log.info("user found in cache for userId {}", userId);
			return gson.fromJson(userInCache, UserResponseDTO.class);
		}
		return null;
	}

	private String getUserKey(int userId) {
		return "USER_"+userId;
	}
}
