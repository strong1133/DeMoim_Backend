package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.ApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyInfoRepository extends JpaRepository<ApplyInfo, Long> {
//    List<ApplyInfo> findByTeamId(Long teamId);
    ApplyInfo findByTeamIdAndMembership(Long teamId, ApplyInfo.Membership membership);

    List<ApplyInfo> findAllByTeamIdAndMembership(Long teamId, ApplyInfo.Membership membership);

    List<ApplyInfo> findAllByUserId(Long userId);

    List<ApplyInfo> findTeamIdByUserId(Long userId);

    List<ApplyInfo> findAllByTeamIdAndMembershipAndApplyState(Long teamId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState);

    int countByUserIdAndApplyState(Long userId, ApplyInfo.ApplyState applyState);




    List<Long> findUserIdByTeamIdAndMembershipAndApplyState(Long teamId, ApplyInfo.Membership membership, ApplyInfo.ApplyState applyState);

    ApplyInfo findByTeamIdAndUserId(Long teamId, Long userId);

    List<ApplyInfo> findAllByTeamId(Long teamId);

    int findByTeamId(Long teamId);
}
