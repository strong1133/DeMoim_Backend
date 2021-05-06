package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.TeamUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamUserInfoRepository extends JpaRepository<TeamUserInfo, Long> {
//    List<TeamUserInfo> findByTeamId(Long teamId);
    TeamUserInfo findByTeamIdAndMembership(Long teamId, TeamUserInfo.Membership membership);

    List<TeamUserInfo> findAllByTeamIdAndMembership(Long teamId, TeamUserInfo.Membership membership);

    TeamUserInfo findByTeamIdAndUserId(Long teamId, Long userId);

    List<TeamUserInfo> findAllByTeamId(Long teamId);
}
