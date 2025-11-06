package com.mercury.pas.controller;

import com.mercury.pas.model.dto.PolicyDtos;
import com.mercury.pas.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Validated
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<PolicyDtos.PolicyResponse> create(@Valid @RequestBody PolicyDtos.CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyDtos.PolicyResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PolicyDtos.PolicyResponse>> byCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(policyService.getByCustomer(customerId));
    }

    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<List<PolicyDtos.PolicyResponse>> byAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(policyService.getByAgent(agentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<PolicyDtos.PolicyResponse> update(@PathVariable Long id, @Valid @RequestBody PolicyDtos.CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        policyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


