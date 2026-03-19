package com.example.bankcards.dto;

import com.example.bankcards.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

public record CreateUserResponse(

        String email,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String firstname,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String lastname,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String middlename,

        Set<RoleType> roles

) {
}
