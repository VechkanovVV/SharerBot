package com.example.botapp;

import com.example.botapp.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotAppApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(BotAppApplication.class, args);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		log.info(config.toString());
	}
}
