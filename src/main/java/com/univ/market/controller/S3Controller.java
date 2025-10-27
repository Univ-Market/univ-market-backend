package com.univ.market.controller;

import com.univ.market.dto.response.UploadUrlResponse;
import com.univ.market.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * S3 파일 업로드 관련 API 엔드포인트를 제공하는 컨트롤러
 * Presigned URL을 생성하여 클라이언트에서 직접 S3에 파일을 업로드할 수 있게 합니다.
 */
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class S3Controller {
    
    private final S3Service s3Service;
    
    /**
     * Presigned URL 생성 API
     * 
     * @param fileName 업로드할 파일 이름
     * @param contentType 파일 MIME 타입
     * @param userId 현재 인증된 사용자 ID
     * @return 업로드 URL 및 최종 파일 URL
     */
    @PostMapping("/url")
    public ResponseEntity<UploadUrlResponse> getPresignedUrl(
            @RequestParam String fileName,
            @RequestParam String contentType,
            @AuthenticationPrincipal Long userId) {
        UploadUrlResponse response = s3Service.generatePresignedUrl(fileName, contentType, userId);
        return ResponseEntity.ok(response);
    }
}