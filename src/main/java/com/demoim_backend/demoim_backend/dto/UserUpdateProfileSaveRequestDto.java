package com.demoim_backend.demoim_backend.dto;

import com.demoim_backend.demoim_backend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateProfileSaveRequestDto {
    private Long id;
    private String nickname;
    private String position;
    private String description;
    private String profileImage;

    public UserUpdateProfileSaveRequestDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.position = user.getPosition();
        this.description = user.getDescription();
        this.profileImage = user.getProfileImage();
    }
}
