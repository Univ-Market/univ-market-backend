package com.univ.market.repository;

import com.univ.market.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 엔티티에 대한 데이터 액세스 인터페이스
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자를 찾는 메서드
     * 
     * @param email 검색할 사용자 이메일
     * @return 해당 이메일을 가진 사용자, 없으면 Optional.empty()
     */
    Optional<User> findByEmail(String email);
    
    /**
     * OAuth 제공자 및 ID로 사용자를 찾는 메서드
     * 소셜 로그인 사용자를 식별하는데 사용됩니다.
     * 
     * @param provider OAuth 제공자 (예: 'kakao')
     * @param id OAuth에서 제공하는 사용자 ID
     * @return 해당 조건의 사용자, 없으면 Optional.empty()
     */
    Optional<User> findByOauthProviderAndOauthId(String provider, String id);
}
