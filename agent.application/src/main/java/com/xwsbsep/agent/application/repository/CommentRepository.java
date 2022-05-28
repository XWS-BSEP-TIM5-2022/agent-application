package com.xwsbsep.agent.application.repository;

import com.xwsbsep.agent.application.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByTitleLike(String title);

}
