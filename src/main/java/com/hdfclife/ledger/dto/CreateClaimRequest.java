package com.hdfclife.ledger.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateClaimRequest(

        @NotBlank(message = "Policy number is required")
        @Pattern(
                regexp = "HDFC-LIFE-[0-9]{4}",
                message = "Policy number must match HDFC-LIFE-####"
        )
        String policyNo,

        @NotNull(message = "Claim amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Claim amount must be greater than 0"
        )
        @DecimalMax(
                value = "500000",
                message = "Claim amount must not exceed 500000"
        )
        BigDecimal claimAmount,

        @NotBlank(message = "Urgency is required")
        @Pattern(
                regexp = "HIGH|MEDIUM|LOW",
                message = "Urgency must be HIGH, MEDIUM or LOW"
        )
        String urgency,

        @Size(
                max = 120,
                message = "Hospital name cannot exceed 120 characters"
        )
        String hospitalName
) {
}
