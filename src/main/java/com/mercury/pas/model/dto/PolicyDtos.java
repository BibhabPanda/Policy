package com.mercury.pas.model.dto;

import com.mercury.pas.model.enums.PolicyStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PolicyDtos {
    public record CreatePolicyRequest(
            @NotNull Long quoteId,
            @NotNull Long agentId,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate
    ) {}

    public record PolicyResponse(
            Long id,
            String policyNumber,
            Long quoteId,
            Long vehicleId,
            Long customerId,
            Long agentId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal premiumAmount,
            PolicyStatus status
    ) {}
}


