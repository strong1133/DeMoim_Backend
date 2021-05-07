package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findById(Long id);

//    List<Team> findAll(Pageable pageable);
//    Page<Team> findAll(Pageable pageable);

    List<Team> findAllById(Long id);
    List<Team> findByLeader(User user);

    Page<Team> findAllByFrontGreaterThan(int num,Pageable PageRequest);
    Page<Team> findAllByBackGreaterThan(int num,Pageable PageRequest);
    Page<Team> findAllByDesignerGreaterThan(int num,Pageable PageRequest);
    Page<Team> findAllByPlannerGreaterThan(int num,Pageable PageRequest);

}
