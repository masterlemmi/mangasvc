package com.lemoncode.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String originsEnv = env.getProperty("ALLOWED_ORIGINS");
        String allowedOrigins = StringUtils.isEmpty(originsEnv)? "http://localhost:4200" : originsEnv;

        System.out.println("Allowed Origin: " + allowedOrigins );
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins(allowedOrigins);
    }
}