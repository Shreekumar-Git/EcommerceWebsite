package com.EcommerceWebsite.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce App API Documentation")
                        .version("1.0.0")
                        .description("OpenAPI documentation for the Ecommerce Website project")
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")
                                .url("https://github.com"))
                );
    }
}
