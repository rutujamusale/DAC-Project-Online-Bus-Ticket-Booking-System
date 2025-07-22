package com.cdacproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Test endpoints")
public class TestController {

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the application is running")
    public String healthCheck() {
        return "Application is running successfully!";
    }
}
