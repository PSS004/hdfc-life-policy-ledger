package com.hdfclife.ledger.config;

import com.hdfclife.ledger.domain.*;
import com.hdfclife.ledger.repo.CustomerRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import com.hdfclife.ledger.repo.RiderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final RiderRepository riderRepository;

    public DataSeeder(CustomerRepository customerRepository,
                      PolicyRepository policyRepository,
                      RiderRepository riderRepository) {
        this.customerRepository = customerRepository;
        this.policyRepository = policyRepository;
        this.riderRepository = riderRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (policyRepository.count() > 0) {
            return;
        }

        Customer anita = customerRepository.save(
                new Customer("Anita Sharma", "anita@example.com"));

        Customer rahul = customerRepository.save(
                new Customer("Rahul Mehta", "rahul@example.com"));

        Customer priya = customerRepository.save(
                new Customer("Priya Nair", "priya@example.com"));

        Customer vikram = customerRepository.save(
                new Customer("Vikram Singh", "vikram@example.com"));

        Customer sneha = customerRepository.save(
                new Customer("Sneha Patel", "sneha@example.com"));

        policyRepository.save(new Policy(
                "HDFC-LIFE-1001", anita, PolicyType.TERM,
                new BigDecimal("18500"), PolicyStatus.Active));

        policyRepository.save(new Policy(
                "HDFC-LIFE-1002", rahul, PolicyType.ULIP,
                new BigDecimal("42000"), PolicyStatus.Active));

        policyRepository.save(new Policy(
                "HDFC-LIFE-1003", priya, PolicyType.ENDOWMENT,
                new BigDecimal("27000"), PolicyStatus.Lapsed));

        policyRepository.save(new Policy(
                "HDFC-LIFE-1004", vikram, PolicyType.TERM,
                new BigDecimal("15200"), PolicyStatus.Active));

        policyRepository.save(new Policy(
                "HDFC-LIFE-1005", sneha, PolicyType.ULIP,
                new BigDecimal("36000"), PolicyStatus.Active));

        policyRepository.save(new Policy(
                "HDFC-LIFE-1006", anita, PolicyType.ENDOWMENT,
                new BigDecimal("22000"), PolicyStatus.Pending));

        Rider accident = riderRepository
                .findByCode("ACCIDENT_COVER")
                .orElseThrow();

        Rider waiver = riderRepository
                .findByCode("WAIVER_OF_PREMIUM")
                .orElseThrow();

        Policy policy = policyRepository
                .findByPolicyNo("HDFC-LIFE-1001")
                .orElseThrow();

        policy.getRiders().add(accident);
        policy.getRiders().add(waiver);
    }
}
