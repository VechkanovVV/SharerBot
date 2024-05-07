package com.example.botapp;

import com.example.botapp.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(ApplicationConfig.class)
@SpringBootApplication
public class BotAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotAppApplication.class, args);
	}

}
