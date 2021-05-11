package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition,Long> {
    List<Exhibition> findAllByExhibitionUserId(Long userId);
    @EntityGraph(attributePaths = "exhibitionUser")
    @Query("SELECT DISTINCT e from Exhibition e")
    Page<Exhibition> findAll(Pageable pageable);
}
