package com.univ.market.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 상품 등록/수정 요청 DTO
 * 클라이언트에서 서버로 전송되는 상품 등록 및 수정 데이터를 담습니다.
 */
@Data
public class ProductRequest {
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
     * 카테고리 ID
     */
    private Long categoryId;
    
    /**
     * 이미지 URL 목록
     */
    private List<String> imageUrls;
}
