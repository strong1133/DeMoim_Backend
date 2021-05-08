package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findById(Long TeamId);

    boolean existsByIdAndProjectState(Long teamId, Team.StateNow StateNow);

//    List<Team> findAll(Pageable pageable);
//    Page<Team> findAll(Pageable pageable);

    List<Team> findAllById(Long TeamId);
}
