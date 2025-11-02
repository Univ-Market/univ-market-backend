package com.univ.market.dto.request;

import lombok.Data;

/**
 * 카테고리 등록 요청 DTO
 * 클라이언트에서 서버로 전송되는 카테고리 등록 데이터를 담습니다.
 */
@Data
public class CategoryRequest {
    /**
     * 카테고리 이름
     */
    private String name;
}
