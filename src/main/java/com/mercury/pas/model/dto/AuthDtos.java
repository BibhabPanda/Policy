package com.mercury.pas.model.dto;

import com.mercury.pas.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AuthDtos {
    public record RegisterRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email @NotBlank String email,
            @Size(min = 6) @NotBlank String password,
            @NotNull Role role,
            LocalDate dob,
            String licenseNumber
    ) {}

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    public record AuthResponse(String accessToken, String tokenType) {}

    public record ResetPasswordRequest(@Email @NotBlank String email, @Size(min = 6) @NotBlank String newPassword) {}
}


