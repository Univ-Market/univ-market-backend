package com.univ.market.security;

import com.univ.market.domain.User;
import com.univ.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 커스텀 사용자 정보 서비스
 * Spring Security의 UserDetailsService를 구현하여 사용자 정보를 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    /**
     * 이메일을 기반으로 사용자 정보를 로드하는 메서드
     * 
     * @param email 사용자 이메일
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                "",
                Collections.emptyList());
    }
    
    /**
     * ID를 기반으로 사용자 정보를 로드하는 메서드
     * 
     * @param id 사용자 ID
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
     */
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id));
        
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                "",
                Collections.emptyList());
    }
}