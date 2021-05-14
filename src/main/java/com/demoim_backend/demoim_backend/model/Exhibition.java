package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.ExhibitionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Entity
public class Exhibition extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    //    @Column(nullable = false)
    @Column(nullable = true, length = 400)
    private String thumbnail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User exhibitionUser;

    @Builder
    public Exhibition(String title, String contents, String thumbnail) {
        this.title = title;
        this.contents = contents;
        this.thumbnail = thumbnail;
    }

    public void setUser(User user){
        //기존에 있던 exhibition제거
        if(this.exhibitionUser != null){
            this.exhibitionUser.getExhibitions().remove(this);
        }
        this.exhibitionUser = user;
        exhibitionUser.getExhibitions().add(this);

    }

    public void update(ExhibitionDto exhibitionDto){
        this.title = exhibitionDto.getTitle();
        this.contents = exhibitionDto.getContents();
        this.thumbnail = exhibitionDto.getThumbnail();
    }

    public void updateImg(String thumbnail){
        this.thumbnail = thumbnail;
    }

}
