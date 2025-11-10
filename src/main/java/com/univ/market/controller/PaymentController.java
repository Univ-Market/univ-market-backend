package com.univ.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.univ.market.dto.request.VerifyPaymentRequest;
import com.univ.market.service.PaymentService;

// principal에서 id 꺼내는 방식은 프로젝트에 맞게 조정하세요.
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyPaymentRequest req,@AuthenticationPrincipal Long userId) {
        /** 테스트용으로잠가버
        if (userId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
            */ 
        paymentService.verifyAndReserve(req, userId);
        return ResponseEntity.ok().build();
    }
}