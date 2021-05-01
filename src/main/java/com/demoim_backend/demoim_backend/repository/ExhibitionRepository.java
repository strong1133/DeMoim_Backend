package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition,Long> {

    @EntityGraph(attributePaths = "exhibitionUser")
    @Query("SELECT DISTINCT e from Exhibition e")
    Page<Exhibition> findAll(Pageable pageable);
}
