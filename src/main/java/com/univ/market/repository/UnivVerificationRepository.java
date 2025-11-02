package com.univ.market.repository;

import com.univ.market.domain.UnivVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 대학교 인증 엔티티에 대한 데이터 액세스 인터페이스
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface UnivVerificationRepository extends JpaRepository<UnivVerification, Long> {
    
    /**
     * 이메일과 인증 코드로 인증 정보를 조회하는 메서드
     * 사용자가 입력한 인증 코드가 유효한지 확인할 때 사용합니다.
     * 
     * @param email 대학교 이메일
     * @param verificationCode 인증 코드
     * @return 해당 조건의 인증 정보, 없으면 Optional.empty()
     */
    Optional<UnivVerification> findByEmailAndVerificationCode(String email, String verificationCode);
}