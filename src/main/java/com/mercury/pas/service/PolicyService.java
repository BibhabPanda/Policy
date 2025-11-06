package com.mercury.pas.service;

import com.mercury.pas.model.dto.PolicyDtos;

import java.util.List;

public interface PolicyService {
    PolicyDtos.PolicyResponse create(PolicyDtos.CreatePolicyRequest request);
    PolicyDtos.PolicyResponse getById(Long id);
    List<PolicyDtos.PolicyResponse> getByCustomer(Long customerId);
    List<PolicyDtos.PolicyResponse> getByAgent(Long agentId);
    PolicyDtos.PolicyResponse update(Long id, PolicyDtos.CreatePolicyRequest request);
    void delete(Long id);
}


