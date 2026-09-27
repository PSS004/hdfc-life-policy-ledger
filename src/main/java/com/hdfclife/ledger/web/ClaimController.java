package com.hdfclife.ledger.web;

import com.hdfclife.ledger.dto.ClaimResponse;
import com.hdfclife.ledger.dto.CreateClaimRequest;
import com.hdfclife.ledger.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<ClaimResponse> create(
            @Valid @RequestBody CreateClaimRequest request) {

        ClaimResponse response = claimService.create(request);

        return ResponseEntity
                .created(URI.create("/api/claims/" + response.claimNo()))
                .body(response);
    }

    @GetMapping("/{claimNo}")
    public ClaimResponse getByClaimNo(
            @PathVariable String claimNo) {

        return claimService.findByClaimNo(claimNo);
    }
}
