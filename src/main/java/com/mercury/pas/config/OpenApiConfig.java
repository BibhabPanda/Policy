package com.mercury.pas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        final String bearerKey = "bearer-jwt";
        return new OpenAPI()
                .info(new Info()
                        .title("Mercury PAS API")
                        .description("Auto Insurance Policy Administration System - API")
                        .version("0.0.1"))
                .addSecurityItem(new SecurityRequirement().addList(bearerKey))
                .components(new Components().addSecuritySchemes(bearerKey,
                        new SecurityScheme()
                                .name(bearerKey)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}


