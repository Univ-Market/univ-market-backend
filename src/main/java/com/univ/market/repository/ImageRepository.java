package com.univ.market.repository;

import com.univ.market.domain.Image;
import com.univ.market.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 이미지 엔티티에 대한 데이터 액세스를 제공하는 리포지토리
 * Spring Data JPA를 활용한 CRUD 작업을 수행합니다.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    /**
     * 특정 상품에 속한 모든 이미지를 조회하는 메서드
     * 
     * @param product 조회할 상품
     * @return 상품에 속한 이미지 목록
     */
    List<Image> findByProduct(Product product);
    
    /**
     * 특정 상품에 속한 모든 이미지를 조회하는 메서드 (ID 기준)
     * 
     * @param productId 조회할 상품의 ID
     * @return 상품에 속한 이미지 목록
     */
    List<Image> findByProductId(Long productId);
    
    /**
     * 특정 상품에 속한 모든 이미지를 삭제하는 메서드
     * 
     * @param product 삭제할 이미지가 속한 상품
     */
    void deleteByProduct(Product product);
    
    /**
     * 특정 상품에 속한 모든 이미지를 삭제하는 메서드 (ID 기준)
     * 
     * @param productId 삭제할 이미지가 속한 상품의 ID
     */
    void deleteByProductId(Long productId);
}