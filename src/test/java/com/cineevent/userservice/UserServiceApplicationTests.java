package com.cineevent.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import com.cineevent.userservice.configuration.RabbitMQConfig;

@SpringBootTest
@ComponentScan(basePackages = { "com.cineevent.userservice" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { RabbitMQConfig.class}) })
@Sql({ "classpath:test_schema.sql", "classpath:test_data.sql" })
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
