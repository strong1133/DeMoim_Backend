package com.demoim_backend.demoim_backend.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Setter
@Entity
@NoArgsConstructor
public class TeamUserInfo {

    /*
    가지고 있어야할 element목록
    id(PK), userId(FK), teamId(FK), leader, member, isAccepted
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "teamUserInfo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    @Column
    private boolean isAccepted;


    public enum Membership {
        Leader, Member //Member라는 element 피하는게 좋을수도..
    }



}
