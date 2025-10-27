package com.univ.market.repository;

import com.univ.market.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 채팅방 엔티티에 대한 데이터 액세스 인터페이스
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    /**
     * 구매자 ID로 해당 사용자가 참여한 채팅방 목록을 조회하는 메서드
     * 
     * @param buyerId 구매자 ID
     * @return 해당 구매자가 참여한 채팅방 목록
     */
    List<ChatRoom> findByBuyerId(Long buyerId);
    
    /**
     * 판매자 ID로 해당 사용자가 참여한 채팅방 목록을 조회하는 메서드
     * 
     * @param sellerId 판매자 ID
     * @return 해당 판매자가 등록한 상품과 관련된 채팅방 목록
     */
    List<ChatRoom> findByProductSellerId(Long sellerId);
    
    /**
     * 상품 ID와 구매자 ID로 특정 채팅방을 조회하는 메서드
     * 상품과 구매자가 정해지면 채팅방은 하나만 존재해야 합니다.
     * 
     * @param productId 상품 ID
     * @param buyerId 구매자 ID
     * @return 해당 조건의 채팅방, 없으면 Optional.empty()
     */
    Optional<ChatRoom> findByProductIdAndBuyerId(Long productId, Long buyerId);
}
