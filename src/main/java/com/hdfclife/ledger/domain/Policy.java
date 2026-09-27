package com.hdfclife.ledger.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_no", nullable = false, unique = true, length = 20)
    private String policyNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 20)
    private PolicyType productType;

    @Column(name = "base_premium", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePremium;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PolicyStatus status;

    @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
    private List<Claim> claims = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "policy_riders",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "rider_id")
    )
    private List<Rider> riders = new ArrayList<>();

    protected Policy() {
    }

    public Policy(
            String policyNo,
            Customer customer,
            PolicyType productType,
            BigDecimal basePremium,
            PolicyStatus status
    ) {
        this.policyNo = policyNo;
        this.customer = customer;
        this.productType = productType;
        this.basePremium = basePremium;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PolicyType getProductType() {
        return productType;
    }

    public BigDecimal getBasePremium() {
        return basePremium;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public List<Rider> getRiders() {
        return riders;
    }
}
