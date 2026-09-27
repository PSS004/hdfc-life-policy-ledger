package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
import com.hdfclife.ledger.service.ClaimService;
import com.hdfclife.ledger.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies", description = "Policy management APIs")
public class PolicyController {

    private final PolicyService policyService;
    private final ClaimService claimService;

    public PolicyController(
            PolicyService policyService,
            ClaimService claimService
    ) {
        this.policyService = policyService;
        this.claimService = claimService;
    }

    @GetMapping
    @Operation(summary = "Get policies")
    public List<PolicyResponse> getPolicies(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String customer
    ) {

        if (status != null) {
            return policyService.findByStatus(status);
        }

        if (type != null) {
            return policyService.findByType(type);
        }

        if (customer != null) {
            return policyService.findByCustomer(customer);
        }

        return policyService.findAll();
    }

    @GetMapping("/{policyNo}")
    @Operation(summary = "Get policy by policy number")
    public PolicyResponse getPolicy(
            @PathVariable String policyNo
    ) {
        return policyService.findByPolicyNo(policyNo);
    }

    @GetMapping("/search")
    @Operation(summary = "Find policies by minimum premium")
    public List<PolicyResponse> searchPolicies(
            @RequestParam BigDecimal minPremium
    ) {
        return policyService.findByMinimumPremium(minPremium);
    }

    @PostMapping
    @Operation(
            summary = "Create a policy",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Policy created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Policy already exists")
            }
    )
    public ResponseEntity<PolicyResponse> createPolicy(
            @Valid @RequestBody CreatePolicyRequest request
    ) {

        PolicyResponse response = policyService.create(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/policies/" + response.policyNo()
                ))
                .body(response);
    }

    @DeleteMapping("/{policyNo}")
    @Operation(summary = "Delete a policy")
    public ResponseEntity<Void> deletePolicy(
            @PathVariable String policyNo
    ) {

        policyService.delete(policyNo);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{policyNo}/claims")
    @Operation(summary = "Get claims for a policy")
    public List<ClaimResponse> getClaims(
            @PathVariable String policyNo
    ) {

        return claimService.findByPolicy(policyNo);
    }
}

