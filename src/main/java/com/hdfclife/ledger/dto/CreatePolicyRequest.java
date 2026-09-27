package com.hdfclife.ledger.dto;

import com.hdfclife.ledger.validation.ValidPolicyType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePolicyRequest(

        @NotBlank(message = "Policy number is required")
        @Pattern(
                regexp = "HDFC-LIFE-[0-9]{4}",
                message = "Policy number must match HDFC-LIFE-####"
        )
        String policyNo,

        @NotBlank(message = "Customer name is required")
        @Size(
                min = 2,
                max = 120,
                message = "Customer name must contain 2 to 120 characters"
        )
        String customer,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Policy type is required")
        @ValidPolicyType
        String type,

        @NotNull(message = "Base premium is required")
        @DecimalMin(
                value = "0.01",
                message = "Base premium must be greater than 0"
        )
        BigDecimal basePremium,

        @NotBlank(message = "Status is required")
        @Pattern(
                regexp = "Active|Lapsed|Pending",
                message = "Status must be Active, Lapsed or Pending"
        )
        String status
) {
}
