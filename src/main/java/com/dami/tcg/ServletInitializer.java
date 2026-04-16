package com.dami.tcg;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet initializer for deploying the TCG application to an external servlet container.
 * <p>
 * This class extends {@link SpringBootServletInitializer} to support WAR-based deployment
 * in traditional application servers (e.g., Apache Tomcat), as an alternative to the
 * embedded server provided by Spring Boot.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Configures the application when deployed as a WAR file.
	 * <p>
	 * Registers the {@link TcgApplication} class as the primary Spring Boot configuration source.
	 * </p>
	 *
	 * @param application the {@link SpringApplicationBuilder} used to configure the application
	 * @return the configured {@link SpringApplicationBuilder} instance
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TcgApplication.class);
	}

}
