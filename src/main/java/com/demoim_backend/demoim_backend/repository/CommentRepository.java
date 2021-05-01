package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySmallTalkId(Long smallTalkId);
    List<Comment> findByExhibitionId(Long exhibitionId);
}
