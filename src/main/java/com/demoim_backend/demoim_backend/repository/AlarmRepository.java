package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUserId(Long userId);
}
