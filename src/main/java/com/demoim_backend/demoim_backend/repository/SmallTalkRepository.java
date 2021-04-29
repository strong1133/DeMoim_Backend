package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.SmallTalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmallTalkRepository extends JpaRepository<SmallTalk,Long> {
}
