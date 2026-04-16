package com.dami.tcg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the TCG (Trading Card Game) Spring Boot application.
 * <p>
 * This class bootstraps the application using Spring Boot's auto-configuration
 * and component scanning mechanisms.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@SpringBootApplication
public class TcgApplication {

	/**
	 * Application main method that launches the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(TcgApplication.class, args);
	}

}
