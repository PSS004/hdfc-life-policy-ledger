package com.hdfclife.ledger.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_no", nullable = false, unique = true, length = 20)
    private String claimNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ClaimStatus status;

    protected Claim() {
    }

    public Claim(
            String claimNo,
            Policy policy,
            BigDecimal amount,
            Urgency urgency,
            ClaimStatus status
    ) {
        this.claimNo = claimNo;
        this.policy = policy;
        this.amount = amount;
        this.urgency = urgency;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public Policy getPolicy() {
        return policy;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public ClaimStatus getStatus() {
        return status;
    }
}
