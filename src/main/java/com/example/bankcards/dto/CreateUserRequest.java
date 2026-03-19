package com.example.bankcards.dto;

import com.example.bankcards.entity.RoleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

import static com.example.bankcards.config.ApplicationConstant.*;

public record CreateUserRequest(

        @NotEmpty
        @Pattern(regexp = REGEXP_EMAIL, message = MESSAGE_INCORRECT_EMAIL)
        String email,

        @NotEmpty
        @Length(min = 8, max = 64, message = MESSAGE_PASSWORD_LENGTH_DOES_NOT_REQUIREMENTS)
        @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS)
        String password,

        @NotEmpty
        @Pattern(regexp = REGEXP_NAME, message = MESSAGE_INCORRECT_NAME)
        String firstname,

        @NotEmpty
        @Pattern(regexp = REGEXP_NAME, message = MESSAGE_INCORRECT_NAME)
        String lastname,

        @Pattern(regexp = REGEXP_NAME, message = MESSAGE_INCORRECT_NAME)
        String middlename,

        @NotEmpty(message = MESSAGE_NO_PARAMETER_AVAILABLE)
        Set<RoleType> roles

) {
}
