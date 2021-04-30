package com.demoim_backend.demoim_backend.model;

import javax.persistence.*;

@Entity
public class Exhibition extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String thumbnail;


}
