package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.dto.ClaimDtos;
import com.mercury.pas.model.entity.Claim;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.ClaimStatus;
import com.mercury.pas.repository.ClaimRepository;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.ClaimService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository, UserRepository userRepository, ModelMapper mapper) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ClaimDtos.ClaimResponse fileClaim(ClaimDtos.FileClaimRequest request) {
        Policy policy = policyRepository.findById(request.policyId()).orElseThrow(() -> new NotFoundException("Policy not found"));
        User customer = userRepository.findById(request.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Claim claim = Claim.builder()
                .claimNumber("MER-CLM-" + UUID.randomUUID())
                .policy(policy)
                .customer(customer)
                .description(request.description())
                .status(ClaimStatus.NEW)
                .createdAt(OffsetDateTime.now())
                .build();
        claimRepository.save(claim);
        return mapper.map(claim, ClaimDtos.ClaimResponse.class);
    }

    @Override
    public ClaimDtos.ClaimResponse getById(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new NotFoundException("Claim not found"));
        return mapper.map(claim, ClaimDtos.ClaimResponse.class);
    }

    @Override
    public List<ClaimDtos.ClaimResponse> getByPolicy(Long policyId) {
        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new NotFoundException("Policy not found"));
        return claimRepository.findByPolicy(policy).stream().map(c -> mapper.map(c, ClaimDtos.ClaimResponse.class)).toList();
    }

    @Override
    public ClaimDtos.ClaimResponse uploadDocument(Long claimId, ClaimDtos.UploadDocumentRequest request) {
        Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new NotFoundException("Claim not found"));
        claim.getDocumentPaths().add(request.path());
        claimRepository.save(claim);
        return mapper.map(claim, ClaimDtos.ClaimResponse.class);
    }
}


