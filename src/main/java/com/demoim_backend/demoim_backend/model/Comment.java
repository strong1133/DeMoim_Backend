package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.CommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = true)
    private Long smallTalkId;

    @Column(nullable = true)
    private Long exhibitionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User commentUser;

    @Builder
    public Comment(CommentRequestDto commentRequestDto, User user){
        this.comments = commentRequestDto.getComments();
        this.commentUser = user;
    }
}
