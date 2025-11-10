package com.univ.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.univ.market.dto.request.VerifyPaymentRequest;
import com.univ.market.infra.PortOneClient;
import com.univ.market.repository.*;
// ↓ 실제 경로에 맞춰 수정하세요
import com.univ.market.domain.*;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PortOneClient portOneClient;
    private final ProductRepository productRepository;
    private final UserRepository userRepository; // buyer 세팅용

    @Transactional
    public void verifyAndReserve(VerifyPaymentRequest req, Long buyerId) {
        // 1) 상품 조회 + 판매중(WAITING) 확인
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        if (product.getStatus() != Product.ProductStatus.WAITING) {
            throw new IllegalStateException("결제할 수 없는 상품 상태입니다.");
        }

        // 2) 금액 일치 확인 (price는 int)
        if (product.getPrice() != req.getAmount()) {
            throw new IllegalStateException("요청 금액이 상품 금액과 일치하지 않습니다.");
        }

        System.out.println("🔹 요청 merchant_uid: " + req.getMerchant_uid());
        System.out.println("🔹 요청 amount: " + req.getAmount());

        // 3) 서버사이드 결제 검증
        String token = portOneClient.issueToken();
        Map<?,?> payResult = portOneClient.getPaymentByImpUid(token, req.getImp_uid());
        Map<?,?> resp = (Map<?,?>) payResult.get("response");

        System.out.println("🔹 PortOne 응답: " + resp);

        if (resp == null) throw new IllegalStateException("결제 응답 없음");

        String status = (String) resp.get("status");           // paid / ready / cancelled ...
        Integer paidAmount = ((Number) resp.get("amount")).intValue();
        String merchantUid = (String) resp.get("merchant_uid");

        if (!"paid".equalsIgnoreCase(status))                 throw new IllegalStateException("미결제 상태");
        if (!req.getMerchant_uid().equals(merchantUid))       throw new IllegalStateException("주문번호 불일치");
        if (!req.getAmount().equals(paidAmount))              throw new IllegalStateException("결제 금액 불일치");

        // 4) 예약 처리: buyer 세팅 + 상태 변경
        /** 
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalStateException("구매자 정보를 찾을 수 없습니다."));
        product.setBuyer(buyer);
        product.setStatus(Product.ProductStatus.RESERVED);
        */
        if (buyerId != null) {
            User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalStateException("구매자 정보를 찾을 수 없습니다."));
            product.setBuyer(buyer);
        }
        product.setStatus(Product.ProductStatus.COMPLETED);
        // @Transactional 더티체킹으로 저장됨
    }
}
