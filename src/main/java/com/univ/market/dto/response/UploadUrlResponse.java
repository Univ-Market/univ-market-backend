package com.univ.market.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 파일 업로드 URL 응답 DTO
 * S3 Presigned URL 생성 결과를 담습니다.
 */
@Data
@Builder
public class UploadUrlResponse {
    /**
     * 파일 업로드를 위한 Presigned URL
     */
    private String uploadUrl;
    
    /**
     * 업로드 완료 후 접근 가능한 파일 URL
     */
    private String fileUrl;

    /**
     * 이미지 URL 목록
     */
    private List<String> imageUrls;
}