package com.martynenko.anton.company.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI companyOpenAPI() {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearer-jwt",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization")))
        .info(new Info().title("Company API")
            .description("Company API application")
            .version("v1.0.0")
            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
        .addSecurityItem(
            new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
  }
}
