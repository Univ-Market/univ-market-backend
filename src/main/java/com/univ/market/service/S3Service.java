package com.univ.market.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.univ.market.dto.response.UploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * AWS S3 관련 기능을 처리하는 서비스 클래스
 * 파일 업로드를 위한 Presigned URL 생성 및 파일 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class S3Service {
    
    private final AmazonS3 amazonS3;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;
    
    /**
     * S3 Presigned URL을 생성하는 메서드
     * 클라이언트에서 직접 S3에 파일을 업로드할 수 있도록 서명된 URL을 제공합니다.
     * 
     * @param fileName 파일 이름
     * @param contentType 파일 MIME 타입
     * @param userId 사용자 ID (폴더 경로에 사용)
     * @return 업로드 URL 및 최종 파일 URL이 포함된 응답 객체
     */
    public UploadUrlResponse generatePresignedUrl(String fileName, String contentType, Long userId) {
        // 파일명 중복 방지를 위한 UUID 생성
        String fileKey = "images/" + userId + "/" + UUID.randomUUID() + "_" + fileName;
        
        // URL 만료 시간 설정 (5분)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5;
        expiration.setTime(expTimeMillis);
        
        // PreSigned URL 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration)
                .withContentType(contentType);
        
        String uploadUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileKey;
        
        return UploadUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .fileUrl(fileUrl)
                .build();
    }
    
    /**
     * S3에서 파일을 삭제하는 메서드
     * 
     * @param fileUrl 삭제할 파일의 URL
     */
    public void deleteFile(String fileUrl) {
        // URL에서 파일 키 추출
        String fileKey = fileUrl.substring(fileUrl.indexOf(".com/") + 5);
        // 파일 삭제
        amazonS3.deleteObject(bucketName, fileKey);
    }
}