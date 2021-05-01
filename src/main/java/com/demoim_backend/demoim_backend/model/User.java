package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileRequestDto;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileSaveDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // email_형식

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname; // 사용자 이름

    @Column(nullable = false)
    private String position; //(여러개일경우 List<String>)

    @Column(nullable = true)
    private String desc; // 자기소개

    @Column(nullable = true)
    private String profileImage; //일단 이거는 사항 바꾼거 프론트에 알려주기(0426 17:10)

    //    @JoinColumn
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Team> teams = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "exhibitionUser",fetch = FetchType.LAZY)
    private List<Exhibition> exhibitions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "smallTalkUser", fetch = FetchType.LAZY)
    private List<SmallTalk> smallTalks = new ArrayList<>();




    // 회원가입 생성자
    public User(SignupRequestDto signupRequestDto){
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.nickname = signupRequestDto.getNickname();
        this.position = signupRequestDto.getPosition();
        this.desc = "안녕하세요 " + signupRequestDto.getNickname() +"님";
    }

    // 유저 정보 수정
    public void update(UserUpdateProfileSaveDto userUpdateProfileSaveDto){
        this.nickname = userUpdateProfileSaveDto.getNickname();
        this.position = userUpdateProfileSaveDto.getPosition();
        this.desc = userUpdateProfileSaveDto.getDesc();
        this.profileImage = userUpdateProfileSaveDto.getProfileImage();
    }
    public void updateImg(String profileImage){
        this.profileImage = profileImage;
    }

}
