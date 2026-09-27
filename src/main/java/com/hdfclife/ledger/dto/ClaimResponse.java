package com.hdfclife.ledger.dto;

import com.hdfclife.ledger.domain.Claim;

import java.math.BigDecimal;

public record ClaimResponse(
        String claimNo,
        String policyNo,
        BigDecimal claimAmount,
        String urgency,
        String status
) {

    public static ClaimResponse from(Claim claim) {

        return new ClaimResponse(
                claim.getClaimNo(),
                claim.getPolicy().getPolicyNo(),
                claim.getAmount(),
                claim.getUrgency().name(),
                claim.getStatus().name()
        );
    }
}
