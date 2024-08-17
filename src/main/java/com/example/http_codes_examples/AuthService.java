package com.example.http_codes_examples;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean isAuthenticated(String authHeader) {
        // Example: validate the Authorization header
        return authHeader != null && authHeader.startsWith("Bearer ");
    }
}