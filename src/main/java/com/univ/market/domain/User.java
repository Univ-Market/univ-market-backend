package com.univ.market.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 엔티티 클래스
 * 사용자 정보와 인증 상태를 관리합니다.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 사용자 고유 식별자(ID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 사용자 이메일 주소
     */
    private String email;
    
    /**
     * 사용자 닉네임
     */
    private String nickname;
    
    /**
     * 프로필 이미지 URL
     */
    private String profileImage;
    
    /**
     * 사용자의 대학교 이름 (인증 후 설정됨)
     */
    private String universityName;
    
    /**
     * 대학교 인증 여부
     */
    private boolean isVerified;
    
    /**
     * OAuth 제공자 (예: 'kakao')
     */
    private String oauthProvider;
    
    /**
     * OAuth에서 제공하는 고유 ID
     */
    private String oauthId;
    
    /**
     * 생성 일시 (변경 불가)
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 수정 일시
     */
    private LocalDateTime updatedAt;
    
    /**
     * 엔티티 생성 시 자동으로 호출되는 메서드
     * 생성 일시와 수정 일시를 현재 시간으로 설정합니다.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * 엔티티 수정 시 자동으로 호출되는 메서드
     * 수정 일시를 현재 시간으로 업데이트합니다.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}