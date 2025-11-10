package com.univ.market.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VerifyPaymentRequest {
    private String imp_uid;       // 포트원 결제건 고유 ID
    private String merchant_uid;  // 우리가 생성한 주문번호
    private Long productId;       // 결제한 상품 ID
    private Integer amount;       // 프론트가 결제창에 넣은 금액(검증용)
}