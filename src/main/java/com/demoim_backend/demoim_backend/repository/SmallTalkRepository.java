package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.SmallTalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmallTalkRepository extends JpaRepository<SmallTalk,Long> {

    @EntityGraph(attributePaths = "smallTalkUser")
    @Query("SELECT DISTINCT s from SmallTalk s")
    Page<SmallTalk> findAll(Pageable pageable);

    List<SmallTalk> findAllBySmallTalkUserId(Long userId);
}
