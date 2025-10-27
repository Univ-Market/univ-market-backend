package com.univ.market.repository;

import com.univ.market.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 채팅 메시지 엔티티에 대한 데이터 액세스 인터페이스
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 기본 JpaRepository 메서드만 사용
    // ChatRoom에서 OneToMany로 메시지를 가져오는 경우가 많아 추가 메서드 필요 없음
}