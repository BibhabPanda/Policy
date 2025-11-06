package com.mercury.pas.service;

import com.mercury.pas.model.dto.ClaimDtos;

import java.util.List;

public interface ClaimService {
    ClaimDtos.ClaimResponse fileClaim(ClaimDtos.FileClaimRequest request);
    ClaimDtos.ClaimResponse getById(Long id);
    List<ClaimDtos.ClaimResponse> getByPolicy(Long policyId);
    ClaimDtos.ClaimResponse uploadDocument(Long claimId, ClaimDtos.UploadDocumentRequest request);
}


