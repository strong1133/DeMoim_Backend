package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyInfoRepository extends JpaRepository<ApplyInfo, Long> {
//    List<ApplyInfo> findByTeamId(Long teamId);
    ApplyInfo findByTeamIdAndMembership(Long teamId, ApplyInfo.Membership membership);


//    List<ApplyInfo> findAllByTeamIdAndMembership(Long teamId, ApplyInfo.Membership membership);
    List<ApplyInfo> findAllByUserIdAndApplyStateOrUserIdAndApplyState(Long userId, ApplyInfo.ApplyState applyState, Long userId2, ApplyInfo.ApplyState applyState2);

    List<ApplyInfo> findAllByteamIdAndApplyState(Long teamId, ApplyInfo.ApplyState applyState);
    List<ApplyInfo> findAllByUserId(Long userId);

    List<ApplyInfo> findTeamIdByUserId(Long userId);
    List<ApplyInfo> findTeamIdByUserIdAndMembership(Long userId, ApplyInfo.Membership membership);
    List<ApplyInfo> findTeamIdByUserIdAndMembershipAndApplyState(Long userId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState);
    List<ApplyInfo> findByUserIdAndMembershipAndApplyState(Long userId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState );

    List<ApplyInfo> findAllByTeamIdAndMembershipAndApplyState(Long teamId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState);

    int countByTeamIdAndApplyStateAndUserPositionAndMembership(Long teamId, ApplyInfo.ApplyState applyState, String position, ApplyInfo.Membership membership);
    int countByUserIdAndMembershipAndTeam_ProjectState(Long userId, ApplyInfo.Membership membership, Team.StateNow stateNow);
    int countByUserIdAndMembershipAndApplyStateAndTeam_ProjectState(Long userId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState, Team.StateNow stateNow);


    List<Long> findUserIdByTeamIdAndMembershipAndApplyState(Long teamId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState);

    ApplyInfo findByTeamIdAndUserId(Long teamId, Long userId);
    ApplyInfo findByUserIdAndMembership(Long userId, ApplyInfo.Membership membership);
    ApplyInfo findByUserIdAndMembershipAndTeamProjectStateNot(Long userId, ApplyInfo.Membership membership, Team.StateNow stateNow);

    List<ApplyInfo> findAllByTeamId(Long teamId);

    int findByTeamId(Long teamId);
}
