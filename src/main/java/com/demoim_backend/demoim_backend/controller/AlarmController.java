package com.demoim_backend.demoim_backend.controller;

import com.demoim_backend.demoim_backend.model.Alarm;
import com.demoim_backend.demoim_backend.repository.AlarmRepository;
import com.demoim_backend.demoim_backend.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    //확인용
    @GetMapping("/api/alarm/chk")
    public List<Alarm> getAlarmChk(@RequestParam Long userId){
        return alarmRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 알람 조회
    @GetMapping("/api/alarm")
    public List<Alarm> getAlarm(Authentication authentication){
        return alarmService.getAlarm(authentication);
    }

    // 알람 삭제
    @DeleteMapping("/api/alarm")
    public Map<String, String> deleteAlarm(Authentication authentication, @RequestParam(value ="alarm_id" )Long alarmId){
        return alarmService.deleteAlarm(authentication,alarmId);
    }
}
