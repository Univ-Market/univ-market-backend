package com.univ.market.dto.response;

import com.univ.market.domain.User;
import lombok.Builder;
import lombok.Data;

/**
 * 사용자 응답 DTO
 * 서버에서 클라이언트로 전송되는 사용자 정보를 담습니다.
 */
@Data
@Builder
public class UserResponse {
    /**
     * 사용자 ID
     */
    private Long id;
    
    /**
     * 사용자 이메일
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
     * 대학교 이름
     */
    private String universityName;
    
    /**
     * 대학교 인증 여부
     */
    private boolean isVerified;
    
    /**
     * User 엔티티를 UserResponse DTO로 변환하는 정적 메서드
     * 
     * @param user 변환할 User 엔티티
     * @return 변환된 UserResponse 객체
     */
    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .universityName(user.getUniversityName())
                .isVerified(user.isVerified())
                .build();
    }
}