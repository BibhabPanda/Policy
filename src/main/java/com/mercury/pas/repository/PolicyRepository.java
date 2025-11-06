package com.mercury.pas.repository;

import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByCustomer(User customer);
    List<Policy> findByAgent(User agent);
}


