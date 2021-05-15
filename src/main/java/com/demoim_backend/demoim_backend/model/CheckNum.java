package com.demoim_backend.demoim_backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
public class CheckNum {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String certNumber;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String isChecked;


    @Builder
    public CheckNum(String certNumber, String username, String isChecked) {
        this.certNumber = certNumber;
        this.username = username;
        this.isChecked = "false";
    }

    public void updateIsChecked(){
        this.isChecked = "true";
    }
}
