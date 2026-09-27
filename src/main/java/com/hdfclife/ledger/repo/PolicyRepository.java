package com.hdfclife.ledger.repo;

import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.domain.PolicyStatus;
import com.hdfclife.ledger.domain.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Optional<Policy> findByPolicyNo(String policyNo);

    boolean existsByPolicyNo(String policyNo);

    List<Policy> findByStatusOrderByPolicyNoAsc(
            PolicyStatus status
    );

    List<Policy> findByProductTypeOrderByPolicyNoAsc(
            PolicyType productType
    );

    List<Policy> findByCustomer_FullNameOrderByPolicyNoAsc(
            String fullName
    );

    @Query("""
            SELECT p
            FROM Policy p
            WHERE p.basePremium >= :minPremium
            ORDER BY p.basePremium DESC
            """)
    List<Policy> findWithPremiumAtLeast(
            @Param("minPremium") BigDecimal minPremium
    );
}
