package com.vkr.analytics_service.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://176.98.179.99:8083")
                                .description("test server"),
                        new Server().url("http://localhost:8083")
                        .description("Localhost server")
                ));
    }
}
