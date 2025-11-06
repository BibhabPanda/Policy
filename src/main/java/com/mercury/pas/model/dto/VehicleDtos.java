package com.mercury.pas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleDtos {
    public record VehicleRequest(
            @NotBlank String make,
            @NotBlank String model,
            @NotNull Integer year,
            @NotBlank String vin
    ) {}

    public record VehicleResponse(Long id, String make, String model, Integer year, String vin) {}
}


