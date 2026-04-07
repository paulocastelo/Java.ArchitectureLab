package com.archlab.sample04.contracts;

import com.archlab.sample04.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @Email(message = "must be a well-formed email address")
        @NotBlank(message = "email is required")
        String email,

        @NotBlank(message = "fullName is required")
        @Size(max = 120, message = "fullName must have at most 120 characters")
        String fullName,

        @StrongPassword
        String password) {
}
