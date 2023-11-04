package com.cineevent.userservice.cache;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.cineevent.userservice.dto.response.UserResponseDTO;
import com.cineevent.userservice.services.UserService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
public class UserCacheTest {

    @InjectMocks
    private UserCache userCache;

    @Mock
    private JedisPool jedisPool;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private UserService userService;
    
    @Test
    public void testGetUserFromCache() {
        int userId = 1;
        UserResponseDTO expectedUser = new UserResponseDTO();
        expectedUser.setId(userId);
        expectedUser.setUserName("testUser");

        Jedis jedis = Mockito.mock(Jedis.class);
        when(jedis.get("USER_" + userId)).thenReturn("{\"id\":1,\"userName\":\"testUser\"}");

        when(jedisPool.getResource()).thenReturn(jedis);

        UserResponseDTO userResponseDTO = userCache.getUser(userId);

        assertNotNull(userResponseDTO);
        assertEquals(expectedUser.getId(), userResponseDTO.getId());
        assertEquals(expectedUser.getUserName(), userResponseDTO.getUserName());
    }

    @Test
    public void testSetUserInCache() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(3);
        user.setUserName("newUser");

        Jedis jedis = Mockito.mock(Jedis.class);

        when(jedisPool.getResource()).thenReturn(jedis);

        userCache.setUserInCache(user);

        Mockito.verify(jedis).set("USER_3", "{\"id\":3,\"userName\":\"newUser\"}");
    }

    @Test
    public void testGetUserFromCacheAndRefreshFromDB() {
        int userId = 4;

        Jedis jedis = Mockito.mock(Jedis.class);
        when(jedis.get("USER_" + userId)).thenReturn(null);

        when(jedisPool.getResource()).thenReturn(jedis);

        UserResponseDTO userFromDB = new UserResponseDTO();
        userFromDB.setId(userId);
        userFromDB.setUserName("newUser");

        when(userService.getUserById(userId)).thenReturn(userFromDB);

        UserResponseDTO userResponseDTO = userCache.getUser(userId);

        assertNotNull(userResponseDTO);
        assertEquals(userFromDB.getId(), userResponseDTO.getId());
        assertEquals(userFromDB.getUserName(), userResponseDTO.getUserName());

        // Verify that the user is set in the cache
        Mockito.verify(jedis).set("USER_4", "{\"id\":4,\"userName\":\"newUser\"}");
    }

}

