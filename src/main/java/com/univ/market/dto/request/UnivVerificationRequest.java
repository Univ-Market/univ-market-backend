package com.univ.market.dto.request;

import lombok.Data;

/**
 * 대학교 인증 요청 DTO
 * 클라이언트에서 서버로 전송되는 대학교 인증 데이터를 담습니다.
 */
@Data
public class UnivVerificationRequest {
    /**
     * 대학교 이메일
     */
    private String email;
    
    /**
     * 인증 코드
     */
    private String verificationCode;
}