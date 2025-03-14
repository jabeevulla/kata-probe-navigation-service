package com.codekata.oceanprobe.probenavigationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(SwaggerConfig.class)
public class SwaggerConfigTest {

    @Test
    public void givenSwaggerConfig_whenBeanCreated_thenOpenAPIExists(ApplicationContext context) {
        // When: Retrieving OpenAPI bean from application context
        OpenAPI openAPI = context.getBean(OpenAPI.class);

        // Then: Verify OpenAPI bean is not null
        assertNotNull(openAPI, "Swagger OpenAPI bean should be initialized");
        assertEquals("Ocean Probe Navigation API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getDescription());
    }
}