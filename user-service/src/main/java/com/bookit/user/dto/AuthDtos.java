package com.bookit.user.dto;

public class AuthDtos {

    public record RegisterRequest(String email, String password, String fullName) {}
    public record LoginRequest(String email, String password) {}
    public record AuthResponse(String token, String email) {}
}
