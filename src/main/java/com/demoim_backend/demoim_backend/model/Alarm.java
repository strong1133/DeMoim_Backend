package com.demoim_backend.demoim_backend.model;

import com.demoim_backend.demoim_backend.dto.AlarmRequestDto;
import com.demoim_backend.demoim_backend.dto.AlarmSaveDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String contents;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean checkStateAlarm;

    public Alarm(AlarmRequestDto alarmRequestDto){
        this.userId = alarmRequestDto.getUserId();
        this.contents = alarmRequestDto.getContents();
        this.checkStateAlarm = false;
    }

    public void updateAlarm(AlarmSaveDto alarmSaveDto){
        this.checkStateAlarm = alarmSaveDto.getCheckStateAlarm();
    }
}
