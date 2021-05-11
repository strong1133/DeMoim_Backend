package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.*;
import com.demoim_backend.demoim_backend.model.Comment;
import com.demoim_backend.demoim_backend.model.Exhibition;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.CommentRepository;
import com.demoim_backend.demoim_backend.repository.ExhibitionRepository;
import com.demoim_backend.demoim_backend.repository.SmallTalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SmallTalkRepository smallTalkRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final UserService userService;
    private final AlarmService alarmService;


    // 현재 로그인 user 객체로 찾아주는 매서드
    public User curUser(Authentication authentication) {
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        return user;
    }

    // SmallTalk 객체 찾아주는 매서드
    public SmallTalk findSmallTalk(Long smallTalkId) {
        return smallTalkRepository.findById(smallTalkId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
        );
    }

    // Exhibition 객체 찾아주는 매서드
    public Exhibition findExhibition(Long exhibitionId) {
        return exhibitionRepository.findById(exhibitionId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
        );
    }

    // responseUser 코팅
    public ResponseUserDto responseUser(User user) {
        ResponseUserDto responseUserDto = new ResponseUserDto(user.getId(), user.getUsername(), user.getNickname(), user.getPosition(), user.getDescription(), user.getProfileImage());
        return responseUserDto;
    }

    // response 객체로 만들어주는 매서드
    public CommentResponseDto entityToDto(Comment comment, ResponseUserDto responseUserDto) {
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUserDto);
        return commentResponseDto;
    }


    // SmallTalk Comments 작성
    public CommentResponseDto createCommentForSmallTalk(Authentication authentication, CommentRequestDto commentRequestDto, Long smallTalkId) {

        User user = curUser(authentication);
        ResponseUserDto responseUserDto = responseUser(user);
        Comment comment = new Comment(commentRequestDto, user);
        comment.setSmallTalkId(smallTalkId);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUserDto);

        //알람 생성
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        String commentsAlarm = user.getNickname() + "님 께서 SmallTalk 게시물에 댓글을 남기셨습니다.";
        alarmRequestDto.setUserId(findSmallTalk(smallTalkId).getSmallTalkUser().getId());
        alarmRequestDto.setContents(commentsAlarm);
        if(!user.getId().equals(findSmallTalk(smallTalkId).getSmallTalkUser().getId())){
            alarmService.createAlarm(alarmRequestDto);
        }

        return commentResponseDto;
    }

    // SmallTalk Comments 조회
    public List<CommentResponseDto> getCommentForSmallTalk(Long smallTalkId) {

        List<Comment> commentList = commentRepository.findBySmallTalkId(smallTalkId);
        List<CommentResponseDto> commentResponseDto = new ArrayList<>();
//        ResponseUser responseUser = new ResponseUser()

        for (Comment comment : commentList) {
            User user = comment.getCommentUser();
            ResponseUserDto responseUserDto = responseUser(user);
            commentResponseDto.add(this.entityToDto(comment, responseUserDto));
        }
        return commentResponseDto;
    }


    // Exhibition Comments 작성
    public CommentResponseDto createCommentForExhibition(Authentication authentication, CommentRequestDto commentRequestDto, Long exhibitionId) {
        User user = curUser(authentication);
        ResponseUserDto responseUserDto = responseUser(user);
        Comment comment = new Comment(commentRequestDto, user);
        comment.setExhibitionId(exhibitionId);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUserDto);

        //알람 생성
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        String commentsAlarm = user.getNickname() + "님 께서 "+ findExhibition(exhibitionId).getTitle() +" 게시물에 댓글을 남기셨습니다.";
        alarmRequestDto.setUserId(findExhibition(exhibitionId).getExhibitionUser().getId());
        alarmRequestDto.setContents(commentsAlarm);
        if(!user.getId().equals(findExhibition(exhibitionId).getExhibitionUser().getId())){
            alarmService.createAlarm(alarmRequestDto);
        }
        return commentResponseDto;
    }

    // Exhibition Comments 조회
    public List<CommentResponseDto> getCommentForExhibition(Long exhibitionId) {

        List<Comment> commentList = commentRepository.findByExhibitionId(exhibitionId);
        List<CommentResponseDto> commentResponseDto = new ArrayList<>();

        for (Comment comment : commentList) {
            User user = comment.getCommentUser();
            ResponseUserDto responseUserDto = responseUser(user);
            commentResponseDto.add(this.entityToDto(comment, responseUserDto));
        }
        return commentResponseDto;
    }


    // SmallTalk & Exhibition 댓글수정
    @Transactional
    public CommentResponseDto updateComment(Authentication authentication, CommentRequestDto commentRequestDto, Long id) {
        User user = curUser(authentication);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없습니다.")
        );
        if (!user.getId().equals(comment.getCommentUser().getId())) {
            throw new IllegalArgumentException("자신의 댓글만 수정할 수 있습니다.");
        }
        comment.update(commentRequestDto);
        ResponseUserDto responseUserDto = responseUser(user);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUserDto);
        return commentResponseDto;
    }

    // SmallTalk & Exhibition 댓글수정
    @Transactional
    public Map<String, String> deleteComment(Authentication authentication, Long id) {
        User user = curUser(authentication);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없습니다.")
        );
        if (!user.getId().equals(comment.getCommentUser().getId())) {
            throw new IllegalArgumentException("자신의 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.deleteById(id);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "삭제가 완료 되었습니다.");
        return map;
    }

}

