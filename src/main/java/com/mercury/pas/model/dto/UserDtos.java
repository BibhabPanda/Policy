package com.mercury.pas.model.dto;

import com.mercury.pas.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class UserDtos {
    public record UserResponse(Long id, String firstName, String lastName, String email, Role role, LocalDate dob, String licenseNumber) {}

    public record UpdateProfileRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email @NotBlank String email,
            LocalDate dob,
            String licenseNumber
    ) {}
}


