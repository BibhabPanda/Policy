package com.mercury.pas.model.dto;

import com.mercury.pas.model.enums.QuoteStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class QuoteDtos {
    public record GenerateQuoteRequest(
            @NotNull Long customerId,
            @NotBlank String make,
            @NotBlank String model,
            @NotNull Integer year,
            @NotBlank String vin,
            @NotNull Integer driverAge
    ) {}

    public record SaveQuoteRequest(
            @NotNull Long customerId,
            @NotNull Long vehicleId,
            @NotBlank String coverageDetails,
            @NotNull BigDecimal premiumAmount
    ) {}

    public record QuoteResponse(
            Long id,
            String quoteNumber,
            Long vehicleId,
            Long customerId,
            BigDecimal premiumAmount,
            String coverageDetails,
            QuoteStatus status,
            OffsetDateTime createdAt
    ) {}
}


