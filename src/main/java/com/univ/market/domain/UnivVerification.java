package com.univ.market.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 대학교 인증 엔티티 클래스
 * 사용자의 대학교 인증 정보를 관리합니다.
 */
@Entity
@Table(name = "univ_verifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnivVerification {
    /**
     * 인증 정보 고유 식별자(ID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 대학교 이메일 주소
     */
    private String email;
    
    /**
     * 인증 코드 (이메일로 전송됨)
     */
    private String verificationCode;
    
    /**
     * 인증 완료 여부
     */
    private boolean verified;
    
    /**
     * 인증 코드 만료 시간
     */
    private LocalDateTime expiresAt;
    
    /**
     * 인증을 요청한 사용자 (다대일 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    /**
     * 생성 일시 (변경 불가)
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 엔티티 생성 시 자동으로 호출되는 메서드
     * 생성 일시를 현재 시간으로 설정합니다.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}