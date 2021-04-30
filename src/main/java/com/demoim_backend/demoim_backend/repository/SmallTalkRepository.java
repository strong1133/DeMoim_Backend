package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.SmallTalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmallTalkRepository extends JpaRepository<SmallTalk,Long> {
    List<SmallTalk> findAllBySmallTalkUserId(Long userId);
}
