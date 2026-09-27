package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Claim;
import com.hdfclife.ledger.domain.ClaimStatus;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.domain.Urgency;
import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreateClaimRequest;
import com.hdfclife.ledger.exception.ClaimNotFoundException;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.ClaimRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    public ClaimService(
            ClaimRepository claimRepository,
            PolicyRepository policyRepository
    ) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    @Transactional
    public ClaimResponse create(CreateClaimRequest request) {

        Policy policy = policyRepository
                .findByPolicyNo(request.policyNo())
                .orElseThrow(() ->
                        new PolicyNotFoundException(request.policyNo())
                );

        long nextNumber = claimRepository.count() + 1;

        String claimNo = generateClaimNumber(nextNumber);

        while (claimRepository.findByClaimNo(claimNo).isPresent()) {
            nextNumber++;
            claimNo = generateClaimNumber(nextNumber);
        }

        Claim claim = new Claim(
                claimNo,
                policy,
                request.claimAmount(),
                Urgency.valueOf(request.urgency()),
                ClaimStatus.SUBMITTED
        );

        Claim savedClaim = claimRepository.save(claim);

        return ClaimResponse.from(savedClaim);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> findByPolicy(String policyNo) {

        if (!policyRepository.existsByPolicyNo(policyNo)) {
            throw new PolicyNotFoundException(policyNo);
        }

        return claimRepository
                .findByPolicy_PolicyNoOrderByClaimNoAsc(policyNo)
                .stream()
                .map(ClaimResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClaimResponse findByClaimNo(String claimNo) {

        return claimRepository
                .findByClaimNo(claimNo)
                .map(ClaimResponse::from)
                .orElseThrow(() ->
                        new ClaimNotFoundException(claimNo)
                );
    }

    private String generateClaimNumber(long number) {
        return String.format("CLM-%02d", number);
    }
}
