package com.univ.market.dto.response;

import com.univ.market.domain.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 응답 DTO
 * 서버에서 클라이언트로 전송되는 상품 정보를 담습니다.
 */
@Data
@Builder
public class ProductResponse {
    /**
     * 상품 ID
     */
    private Long id;
    
    /**
     * 상품 제목
     */
    private String title;
    
    /**
     * 상품 설명
     */
    private String description;
    
    /**
     * 상품 가격
     */
    private int price;
    
    /**
     * 상품 상태 (WAITING, RESERVED, COMPLETED)
     */
    private String status;
    

    /**
     * 카테고리 이름
     */
    private String categoryName;
    
    /**
     * 카테고리 ID
     */
    private Long categoryId;
    

    /**
     * 판매자 ID 
     */
    private Long sellerId;
    
    /**
     * 판매자 닉네임
     */
    private String sellerNickname;
    

    /**
     * 구매자 ID (예약 또는 거래 완료 시)
     */
    private Long buyerId;
    
    /**
     * 구매자 닉네임 (예약 또는 거래 완료 시)
     */
    private String buyerNickname;
    

    /**
     * 상품 이미지 URL 목록
     */
    private List<String> imageUrls;
    
    /**
     * 생성 일시
     */
    private LocalDateTime createdAt;
    
    /**
     * 수정 일시
     */
    private LocalDateTime updatedAt;
    
    /**
     * 엔티티를 DTO로 변환하는 정적 팩토리 메소드
     * 
     * @param product 상품 엔티티
     * @return 상품 응답 DTO
     */
    
    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus().name())
                .categoryName(product.getCategory().getName())
                .categoryId(product.getCategory().getId())
                .sellerId(product.getSeller().getId())
                .sellerNickname(product.getSeller().getNickname())
                .buyerId(product.getBuyer() != null ? product.getBuyer().getId() : null)
                .buyerNickname(product.getBuyer() != null ? product.getBuyer().getNickname() : null)
                .imageUrls(product.getImages().stream()
                        .map(image -> image.getImageUrl())
                        .collect(Collectors.toList()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}