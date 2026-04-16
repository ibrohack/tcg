package com.dami.tcg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web MVC configuration class for the TCG application.
 * <p>
 * Configures custom resource handlers to serve static files from external directories,
 * such as player profile images stored outside the application's classpath.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registers custom resource handlers to serve external static resources.
     * <p>
     * Maps the URL pattern {@code /images/players/**} to the external directory
     * {@code data/images/players}, allowing player profile pictures to be served
     * directly from the filesystem.
     * </p>
     *
     * @param registry the {@link ResourceHandlerRegistry} used to register resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("data/images/players");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Serve external directory mapped to /player-images/**
        registry.addResourceHandler("/images/players/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
