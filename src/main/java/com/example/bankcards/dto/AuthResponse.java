package com.example.bankcards.dto;

public record AuthResponse(

        String email,

        String token,

        String refreshToken

) {
}
