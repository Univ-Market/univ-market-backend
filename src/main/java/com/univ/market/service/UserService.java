package com.univ.market.service;

import com.univ.market.domain.UnivVerification;
import com.univ.market.domain.User;
import com.univ.market.dto.request.UnivVerificationRequest;
import com.univ.market.dto.response.UserResponse;
import com.univ.market.repository.UnivVerificationRepository;
import com.univ.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 회원 가입, 로그인, 대학교 인증 등의 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UnivVerificationRepository univVerificationRepository;
    private final EmailService emailService;
    
    /**
     * 카카오 로그인 처리 메서드
     * 기존 사용자면 정보를 반환하고, 새 사용자면 등록합니다.
     * 
     * @param oauthId 카카오에서 제공하는 고유 ID
     * @param email 사용자 이메일
     * @param nickname 사용자 닉네임
     * @return 사용자 정보
     */
    @Transactional
    public User processKakaoLogin(String oauthId, String email, String nickname) {
        // 기존 사용자 확인
        User user = userRepository.findByOauthProviderAndOauthId("kakao", oauthId)
                .orElse(null);
        
        if (user == null) {
            // 새 사용자 등록
            user = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .oauthProvider("kakao")
                    .oauthId(oauthId)
                    .isVerified(false)
                    .build();
            
            user = userRepository.save(user);
        }
        
        return user;
    }
    
    /**
     * ID로 사용자 정보를 조회하는 메서드
     * 
     * @param id 사용자 ID
     * @return 사용자 정보
     * @throws IllegalArgumentException 존재하지 않는 사용자인 경우
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponse.fromEntity(user);
    }
    
    /**
     * 대학교 인증 이메일을 전송하는 메서드
     * 
     * @param userId 사용자 ID
     * @param univEmail 대학교 이메일
     * @throws IllegalArgumentException 존재하지 않는 사용자이거나 유효하지 않은 대학 이메일인 경우
     */
    @Transactional
    public void sendUnivVerificationEmail(Long userId, String univEmail) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 대학 이메일 형식 검증 (간단한 예시)
        if (!univEmail.matches(".*\\.(ac\\.kr|edu)$")) {
            throw new IllegalArgumentException("유효한 대학 이메일이 아닙니다.");
        }
        
        // 인증 코드 생성
        String verificationCode = generateRandomCode();
        
        // DB에 인증 정보 저장
        UnivVerification verification = UnivVerification.builder()
                .email(univEmail)
                .verificationCode(verificationCode)
                .verified(false)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        
        univVerificationRepository.save(verification);
        
        // 인증 이메일 발송
        emailService.sendVerificationEmail(univEmail, verificationCode);
    }
    
    /**
     * 대학교 인증 코드를 확인하는 메서드
     * 
     * @param request 인증 요청 데이터
     * @return 인증 성공 여부
     * @throws IllegalArgumentException 존재하지 않는 인증 정보인 경우
     * @throws IllegalStateException 인증 코드가 만료된 경우
     */
    @Transactional
    public boolean verifyUnivEmail(UnivVerificationRequest request) {
        // 인증 정보 조회
        UnivVerification verification = univVerificationRepository.findByEmailAndVerificationCode(
                request.getEmail(), request.getVerificationCode())
                .orElseThrow(() -> new IllegalArgumentException("인증 정보를 찾을 수 없습니다."));
        
        // 만료 여부 확인
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("인증 코드가 만료되었습니다.");
        }
        
        // 인증 완료 처리
        verification.setVerified(true);
        
        // 사용자 대학 정보 업데이트
        User user = verification.getUser();
        user.setVerified(true);
        
        // 이메일에서 대학 이름 추출 (예: username@snu.ac.kr -> 서울대학교)
        String emailDomain = verification.getEmail().substring(verification.getEmail().indexOf('@') + 1);
        String universityName = extractUniversityName(emailDomain);
        user.setUniversityName(universityName);
        
        userRepository.save(user);
        
        return true;
    }
    
    /**
     * 이메일 도메인에서 대학교 이름을 추출하는 메서드
     * 
     * @param emailDomain 이메일 도메인 (예: snu.ac.kr)
     * @return 대학교 이름 (예: 서울대학교)
     */
    private String extractUniversityName(String emailDomain) {
        // 실제 구현에서는 대학 도메인과 이름을 매핑하는 로직 필요
        // 여기서는 간단한 예시만 제공
        if (emailDomain.contains("snu.ac.kr")) return "서울대학교";
        if (emailDomain.contains("yonsei.ac.kr")) return "연세대학교";
        if (emailDomain.contains("korea.ac.kr")) return "고려대학교";
        // 기본값
        return emailDomain.split("\\.")[0] + "대학교";
    }
    
    /**
     * 6자리 무작위 인증 코드를 생성하는 메서드
     * 
     * @return 생성된 인증 코드
     */
    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 숫자
        return String.valueOf(code);
    }
}