package com.dami.tcg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("data/images/players");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Serve external directory mapped to /player-images/**
        registry.addResourceHandler("/images/players/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
