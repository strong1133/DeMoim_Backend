package com.demoim_backend.demoim_backend.util;

import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
public class NowTeamCount {
    private final ApplyInfoRepository applyInfoRepository;

    public int nowTeamCnt(User user){
        int memberCnt = applyInfoRepository.countByUserIdAndMembershipAndApplyStateAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED,
                Team.StateNow.ACTIVATED)+applyInfoRepository.countByUserIdAndMembershipAndApplyStateAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED,
                Team.StateNow.YET);
        int leadCnt = applyInfoRepository.countByUserIdAndMembershipAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.LEADER, Team.StateNow.ACTIVATED)+
                applyInfoRepository.countByUserIdAndMembershipAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.LEADER, Team.StateNow.YET);
        return memberCnt + leadCnt;
    }
}
