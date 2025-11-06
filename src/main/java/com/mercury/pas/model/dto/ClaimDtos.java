package com.mercury.pas.model.dto;

import com.mercury.pas.model.enums.ClaimStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public class ClaimDtos {
    public record FileClaimRequest(
            @NotNull Long policyId,
            @NotNull Long customerId,
            @NotBlank String description
    ) {}

    public record UploadDocumentRequest(
            @NotBlank String path
    ) {}

    public record ClaimResponse(
            Long id,
            String claimNumber,
            Long policyId,
            Long customerId,
            String description,
            ClaimStatus status,
            List<String> documentPaths,
            OffsetDateTime createdAt
    ) {}
}


