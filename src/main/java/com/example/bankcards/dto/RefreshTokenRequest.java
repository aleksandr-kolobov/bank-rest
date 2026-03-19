package com.example.bankcards.dto;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(

        @NotEmpty
        String token

) {
}
