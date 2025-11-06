package com.mercury.pas.repository;

import com.mercury.pas.model.entity.Claim;
import com.mercury.pas.model.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<Claim> findByClaimNumber(String claimNumber);
    List<Claim> findByPolicy(Policy policy);
}


