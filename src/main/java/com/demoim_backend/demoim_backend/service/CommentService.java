package com.demoim_backend.demoim_backend.service;

import com.demoim_backend.demoim_backend.dto.CommentRequestDto;
import com.demoim_backend.demoim_backend.dto.CommentResponseDto;
import com.demoim_backend.demoim_backend.dto.ResponseUser;
import com.demoim_backend.demoim_backend.dto.SmallTalkResponseDto;
import com.demoim_backend.demoim_backend.model.Comment;
import com.demoim_backend.demoim_backend.model.SmallTalk;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


    // 현재 로그인 user 객체로 찾아주는 매서드
    public User curUser(Authentication authentication) {
        User user = userService.findCurUser(authentication).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );
        return user;
    }

    public ResponseUser responseUser(User user) {
        ResponseUser responseUser = new ResponseUser(user.getId(), user.getUsername(), user.getNickname(), user.getProfileImage());
        return responseUser;
    }

    // response 객체로 만들어주는 매서드
    public CommentResponseDto entityToDto(Comment comment, ResponseUser responseUser) {
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUser);
        return commentResponseDto;
    }


    // SmallTalk Comments 작성
    public CommentResponseDto createCommentForSmallTalk(Authentication authentication, CommentRequestDto commentRequestDto, Long smallTalkId) {
        User user = curUser(authentication);
        ResponseUser responseUser = responseUser(user);
        Comment comment = new Comment(commentRequestDto, user);
        comment.setSmallTalkId(smallTalkId);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUser);
        return commentResponseDto;
    }

    // SmallTalk Comments 조회
    public List<CommentResponseDto> getCommentForSmallTalk(Long smallTalkId) {

        List<Comment> commentList = commentRepository.findBySmallTalkId(smallTalkId);
        List<CommentResponseDto> commentResponseDto = new ArrayList<>();
//        ResponseUser responseUser = new ResponseUser()

        for (Comment comment : commentList) {
            User user = comment.getCommentUser();
            ResponseUser responseUser = responseUser(user);
            commentResponseDto.add(this.entityToDto(comment, responseUser));
        }
        return commentResponseDto;
    }


    // Exhibition Comments 작성
    public CommentResponseDto createCommentForExhibition(Authentication authentication, CommentRequestDto commentRequestDto, Long exhibitionId) {
        User user = curUser(authentication);
        ResponseUser responseUser = responseUser(user);
        Comment comment = new Comment(commentRequestDto, user);
        comment.setExhibitionId(exhibitionId);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUser);
        return commentResponseDto;
    }

    // Exhibition Comments 조회
    public List<CommentResponseDto> getCommentForExhibition(Long exhibitionId) {

        List<Comment> commentList = commentRepository.findByExhibitionId(exhibitionId);
        List<CommentResponseDto> commentResponseDto = new ArrayList<>();

        for (Comment comment : commentList) {
            User user = comment.getCommentUser();
            ResponseUser responseUser = responseUser(user);
            commentResponseDto.add(this.entityToDto(comment, responseUser));
        }
        return commentResponseDto;
    }


    // SmallTalk & Exhibition 댓글수정
    @Transactional
    public CommentResponseDto updateComment(Authentication authentication,CommentRequestDto commentRequestDto,  Long id){
        User user = curUser(authentication);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없습니다.")
        );
        if (!user.getId().equals(comment.getCommentUser().getId())){
            throw new IllegalArgumentException("자신의 댓글만 수정할 수 있습니다.");
        }
        comment.update(commentRequestDto);
        ResponseUser responseUser = responseUser(user);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, responseUser);
        return commentResponseDto;
    }

    // SmallTalk & Exhibition 댓글수정
    @Transactional
    public Map<String, String> deleteComment(Authentication authentication, Long id){
        User user = curUser(authentication);
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 없습니다.")
        );
        if (!user.getId().equals(comment.getCommentUser().getId())){
            throw new IllegalArgumentException("자신의 댓글만 수정할 수 있습니다.");
        }
        commentRepository.deleteById(id);
        Map<String, String> map = new HashMap<>();
        map.put("msg","삭제가 완료 되었습니다.");
        return map;
    }

}

