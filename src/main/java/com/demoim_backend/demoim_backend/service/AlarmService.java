package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.AlarmRequestDto;
import com.demoim_backend.demoim_backend.model.Alarm;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserService userService;

    // 알람 조회
    public List<Alarm> getAlarm(Authentication authentication) {
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        Long userId = user.getId();
        return alarmRepository.findByUserId(userId);
    }

    // 알람 생성
    @Transactional
    public void createAlarm(AlarmRequestDto alarmRequestDto) {
        Alarm alarm = new Alarm(alarmRequestDto);
        alarmRepository.save(alarm);
    }

    // 알람 삭제
    @Transactional
    public Map<String, String> deleteAlarm(Authentication authentication, Long alarmId) {
        Map<String, String> map = new HashMap<>();
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new IllegalArgumentException("해당 알람이 존재하지않습니다.")
        );
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        Long curUser = user.getId();
        Long userId = alarm.getUserId();

        if (!curUser.equals(userId)) {
            throw new IllegalArgumentException(
                    "자신의 알람만 삭제할 수 있습니다."
            );
        }
        alarmRepository.deleteById(alarmId);
        map.put("msg", "Success");
        return map;

    }
}
