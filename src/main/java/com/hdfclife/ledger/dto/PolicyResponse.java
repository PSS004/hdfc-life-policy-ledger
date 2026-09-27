package com.hdfclife.ledger.dto;

import com.hdfclife.ledger.domain.Policy;

import java.math.BigDecimal;
import java.util.List;

public record PolicyResponse(
        String policyNo,
        String customer,
        String email,
        String type,
        BigDecimal basePremium,
        String status,
        List<String> riderCodes
) {

    public static PolicyResponse from(Policy policy) {

        return new PolicyResponse(
                policy.getPolicyNo(),
                policy.getCustomer().getFullName(),
                policy.getCustomer().getEmail(),
                policy.getProductType().name(),
                policy.getBasePremium(),
                policy.getStatus().name(),
                policy.getRiders()
                        .stream()
                        .map(rider -> rider.getCode())
                        .sorted()
                        .toList()
        );
    }
}
