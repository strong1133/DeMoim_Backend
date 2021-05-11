package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.AlarmRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="alarmId")
    private Long alarmId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String contents;

    public Alarm(AlarmRequestDto alarmRequestDto){
        this.userId = alarmRequestDto.getUserId();
        this.contents = alarmRequestDto.getContents();
    }
}
