package com.univ.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyPaymentResponse {
    private boolean success;
    private String message;
}