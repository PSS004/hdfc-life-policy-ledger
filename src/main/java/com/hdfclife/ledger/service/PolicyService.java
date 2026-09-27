package com.hdfclife.ledger.service;

import com.hdfclife.ledger.domain.Customer;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.domain.PolicyStatus;
import com.hdfclife.ledger.domain.PolicyType;
import com.hdfclife.ledger.dto.CreatePolicyRequest;
import com.hdfclife.ledger.dto.PolicyResponse;
import com.hdfclife.ledger.exception.DuplicatePolicyException;
import com.hdfclife.ledger.exception.InvalidRequestException;
import com.hdfclife.ledger.exception.PolicyNotFoundException;
import com.hdfclife.ledger.repo.CustomerRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;

    public PolicyService(
            PolicyRepository policyRepository,
            CustomerRepository customerRepository
    ) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> findAll() {

        return policyRepository
                .findAll(Sort.by(Sort.Direction.ASC, "policyNo"))
                .stream()
                .map(PolicyResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PolicyResponse findByPolicyNo(String policyNo) {

        Policy policy = getPolicy(policyNo);

        return PolicyResponse.from(policy);
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> findByStatus(String status) {

        PolicyStatus policyStatus = parseStatus(status);

        return policyRepository
                .findByStatusOrderByPolicyNoAsc(policyStatus)
                .stream()
                .map(PolicyResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> findByType(String type) {

        PolicyType policyType = parseType(type);

        return policyRepository
                .findByProductTypeOrderByPolicyNoAsc(policyType)
                .stream()
                .map(PolicyResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> findByCustomer(String customer) {

        return policyRepository
                .findByCustomer_FullNameOrderByPolicyNoAsc(customer)
                .stream()
                .map(PolicyResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyResponse> findByMinimumPremium(
            BigDecimal minPremium
    ) {

        if (minPremium == null || minPremium.signum() <= 0) {
            throw new InvalidRequestException(
                    "minPremium must be greater than 0"
            );
        }

        return policyRepository
                .findWithPremiumAtLeast(minPremium)
                .stream()
                .map(PolicyResponse::from)
                .toList();
    }

    @Transactional
    public PolicyResponse create(CreatePolicyRequest request) {

        if (policyRepository.existsByPolicyNo(request.policyNo())) {
            throw new DuplicatePolicyException(request.policyNo());
        }

        Customer customer = customerRepository
                .findByEmail(request.email())
                .orElseGet(() -> customerRepository.save(
                        new Customer(
                                request.customer(),
                                request.email()
                        )
                ));

        Policy policy = new Policy(
                request.policyNo(),
                customer,
                PolicyType.valueOf(request.type()),
                request.basePremium(),
                PolicyStatus.valueOf(request.status())
        );

        Policy savedPolicy = policyRepository.save(policy);

        return PolicyResponse.from(savedPolicy);
    }

    @Transactional
    public void delete(String policyNo) {

        Policy policy = getPolicy(policyNo);

        policyRepository.delete(policy);
    }

    private Policy getPolicy(String policyNo) {

        return policyRepository
                .findByPolicyNo(policyNo)
                .orElseThrow(() ->
                        new PolicyNotFoundException(policyNo)
                );
    }

    private PolicyStatus parseStatus(String value) {

        try {
            return PolicyStatus.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new InvalidRequestException(
                    "Invalid status: " + value
            );
        }
    }

    private PolicyType parseType(String value) {

        try {
            return PolicyType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new InvalidRequestException(
                    "Invalid type: " + value
            );
        }
    }
}