package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.SignupRequestDto;
import com.demoim_backend.demoim_backend.dto.UserUpdateProfileSaveRequestDto;
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

    @Column(nullable = true, length = 400)
    private String profileImage; //일단 이거는 사항 바꾼거 프론트에 알려주기(0426 17:10)

    //fetch 속성은 @ManyToOne에서 주로 쓰인다.
    //영속성 전이를 위해 cascade = CascadeType.ALL을 추가해준다.
//    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TeamUserInfo> teamUserInfos = new ArrayList<TeamUserInfo>();
//    //    @JoinColumn
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Team> teams = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "exhibitionUser",cascade =  CascadeType.ALL)
    private List<Exhibition> exhibitions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "smallTalkUser",cascade =  CascadeType.ALL)
    private List<SmallTalk> smallTalks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "commentUser", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    //연관관계 메소드 User <-> TeamUserInfo
    public void addTeamUserInfo(TeamUserInfo teamUserInfo) {
        teamUserInfos.add(teamUserInfo);
        teamUserInfo.setUser(this);
    }


    // 회원가입 생성자
    public User(SignupRequestDto signupRequestDto){
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.nickname = signupRequestDto.getNickname();
        this.position = signupRequestDto.getPosition();
        this.desc = "안녕하세요 " + signupRequestDto.getNickname() +"님";
    }

    // 유저 정보 수정
    public void update(UserUpdateProfileSaveRequestDto userUpdateProfileSaveDto){
        this.nickname = userUpdateProfileSaveDto.getNickname();
        this.position = userUpdateProfileSaveDto.getPosition();
        this.desc = userUpdateProfileSaveDto.getDesc();
        this.profileImage = userUpdateProfileSaveDto.getProfileImage();
    }
    public void updateImg(String profileImage){
        this.profileImage = profileImage;
    }

}
