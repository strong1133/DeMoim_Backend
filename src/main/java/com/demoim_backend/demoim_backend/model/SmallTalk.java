package com.demoim_backend.demoim_backend.model;

import javax.persistence.*;

@Entity
public class SmallTalk extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
