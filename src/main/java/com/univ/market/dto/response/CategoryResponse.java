package com.univ.market.dto.response;

import com.univ.market.domain.Category;
import lombok.Builder;
import lombok.Data;

/**
 * 카테고리 응답 DTO
 * 서버에서 클라이언트로 전송되는 카테고리 정보를 담습니다.
 */
@Data
@Builder
public class CategoryResponse {
    /**
     * 카테고리 ID
     */
    private Long id;
    
    /**
     * 카테고리 이름
     */
    private String name;
    
    /**
     * Category 엔티티를 CategoryResponse DTO로 변환하는 정적 메서드
     * 
     * @param category 변환할 Category 엔티티
     * @return 변환된 CategoryResponse 객체
     */
    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}