package com.demoim_backend.demoim_backend.util;

import com.demoim_backend.demoim_backend.dto.TeamRequestDto;
import com.demoim_backend.demoim_backend.repository.TeamRepository;
import com.demoim_backend.demoim_backend.s3.FileUploadService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Configuration
public class TeamJsonMapper {
    private final FileUploadService fileUploadService;

    public TeamRequestDto jsonTeamDtoMaker(String requestBody){
        TeamRequestDto teamRequestDto = new TeamRequestDto();
        JSONObject jsonObject = new JSONObject(requestBody);



        teamRequestDto.setTitle(jsonObject.getString("title"));
        teamRequestDto.setRecruit(Long.parseLong(jsonObject.getString("recruit")));
        teamRequestDto.setBegin(Long.parseLong(jsonObject.getString("begin")));
        teamRequestDto.setEnd(Long.parseLong(jsonObject.getString("end")));
        teamRequestDto.setLocation(jsonObject.getString("location"));
        teamRequestDto.setThumbnail(null);
        teamRequestDto.setFront(Integer.parseInt(jsonObject.getString("front")));
        teamRequestDto.setBack(Integer.parseInt(jsonObject.getString("back")));
        teamRequestDto.setDesigner(Integer.parseInt(jsonObject.getString("designer")));
        teamRequestDto.setPlanner(Integer.parseInt(jsonObject.getString("planner")));
        teamRequestDto.setStack(jsonObject.getString("stack"));
        teamRequestDto.setContents(jsonObject.getString("contents"));

        return teamRequestDto;
    }
}


//{
//        "title" : "팀메이킹",
//        "recruit " : "2021-05-02",
//        "begin" : "2021-05-03",
//        "end" : "2021-05-17",
//        "location" : "서울",
//        "thumbnail" : "thumbnailURL",
//        "front" : "2",
//        "back" : "2",
//        "designer" : "2",
//        "planner" : "2",
//        "stack" : "리액트, 스프링",
//        "contents" : "2주간 달릴 사이드프로젝트 인원모집"
//        }