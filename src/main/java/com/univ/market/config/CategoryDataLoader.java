package com.univ.market.config;

import com.univ.market.domain.Category;
import com.univ.market.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                Category.builder().name("전자기기").build(),
                Category.builder().name("도서").build(),
                Category.builder().name("생활용품").build(),
                Category.builder().name("의류").build(),
                Category.builder().name("스포츠/레저").build()
            ));

        }
    }

}