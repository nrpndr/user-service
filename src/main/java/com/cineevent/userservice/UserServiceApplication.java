package com.cineevent.userservice;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UserServiceApplication.class);
		app.setBannerMode(Mode.OFF);
		app.run(args);
	}

}
