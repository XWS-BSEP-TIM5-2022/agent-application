package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.*;
import com.xwsbsep.agent.application.model.Comment;
import com.xwsbsep.agent.application.model.CommentSalary;

import java.util.List;

public interface CommentService {
    void addComment(AddCommentDTO comment) throws Exception;

    List<CommentDTO> findAllByCompanyId(Long companyId);

    List<String> getPositionsByCompanyId(Long companyId);

    void addCommentSalary(CommentSalary comment, Long companyId) throws Exception;

    List<CommentSalaryDTO> getAllSalaryCommentsByCompanyId(Long companyId);

    void addCommentInterview(AddInterviewCommentDTO reqDto) throws Exception;

    List<CommentInterviewDTO> getAllInterviewCommentsByCompanyId(Long companyId);
}
