package com.cineevent.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"classpath:test_schema.sql","classpath:test_data.sql"})
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
