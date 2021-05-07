package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class MypageResponseDto {
    private Long userid;
    private String username;
    private String nickname;
    private String position;
    private String desc;
    private String profileImage;
    private List<Team> leader;

    @Builder
    public MypageResponseDto(User user, @Valid List<Team> leader) {
        this.userid = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.position = user.getPosition();
        this.desc = user.getDesc();
        this.profileImage = user.getProfileImage();
        this.leader = leader;
    }
}
