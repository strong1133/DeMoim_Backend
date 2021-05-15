package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.AlarmRequestDto;
import com.demoim_backend.demoim_backend.dto.AlarmSaveDto;
import com.demoim_backend.demoim_backend.model.Alarm;
import com.demoim_backend.demoim_backend.model.Team;
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

    // 유저 정보 반환
    public User findAlarmUser(Authentication authentication){
        return userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
    }

    // 알람 조회
    @Transactional
    public List<Alarm> getAlarm(Authentication authentication) {
        User user = findAlarmUser(authentication);
        Long userId = user.getId();

        //조회된 알림!
        List<Alarm> alarmList = alarmRepository.findByUserIdOrderByCreatedAtDesc(userId);
        for (Alarm alarm : alarmList) {
            AlarmSaveDto alarmSaveDto = new AlarmSaveDto();
            alarmSaveDto.setCheckStateAlarm(true);
            alarm.updateAlarm(alarmSaveDto);
        }
        return alarmList;
    }

    // 안읽은 알림 카운트
    public Integer beforeCheckAlarmCnt(Authentication authentication) {
        User user = findAlarmUser(authentication);
        Long userId = user.getId();
        return alarmRepository.countAlarmByUserIdAndCheckStateAlarm(userId, false);
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
        User user = findAlarmUser(authentication);
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


    // 알람 전체 삭제
    @Transactional
    public Map<String, String> deleteAllAlarm(Authentication authentication){
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        alarmRepository.deleteAlarmByUserId(user.getId());
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");
        return map;
    }

    //알람 메이커 for Not leader
    public void alarmMaker(String commentsAlarm, User user, Team team) {
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        alarmRequestDto.setUserId(team.getLeader().getId());
        alarmRequestDto.setContents(commentsAlarm);
        if (!user.getId().equals(team.getLeader().getId())) {
            createAlarm(alarmRequestDto);
        }
    }

    //알람 메이커 for leader
    public void alarmMakerForLeader(String commentsAlarm, User leader){
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        alarmRequestDto.setUserId(leader.getId());
        alarmRequestDto.setContents(commentsAlarm);
        createAlarm(alarmRequestDto);
    }
}
