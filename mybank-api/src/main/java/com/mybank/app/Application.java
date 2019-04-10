package com.mybank.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {

		log.info("Launching Spring Application...");
		SpringApplication.run(Application.class, args);
		log.info("Application launched with success!");

	}

}