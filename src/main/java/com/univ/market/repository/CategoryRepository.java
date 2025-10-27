package com.univ.market.repository;

import com.univ.market.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 카테고리 엔티티에 대한 데이터 액세스 인터페이스
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 기본 JpaRepository 메서드만 사용
}
