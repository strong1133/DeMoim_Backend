package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.SmallTalkDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;


@RequiredArgsConstructor
@Getter
@Entity
public class SmallTalk extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User smallTalkUser;

    @Builder
    public SmallTalk(String contents) {
        this.contents = contents;
    }

    public void setUser(User user){

        //기존에 있던 smallTalk을 제거
        if(this.smallTalkUser != null){
            this.smallTalkUser.getSmallTalks().remove(this);
        }
        this.smallTalkUser = user;
        smallTalkUser.getSmallTalks().add(this);
    }

    public void Update(SmallTalkDto smallTalkDto){
        this.contents = smallTalkDto.getContents();
    }

}

