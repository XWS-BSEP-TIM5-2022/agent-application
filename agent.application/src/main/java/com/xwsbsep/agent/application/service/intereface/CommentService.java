package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.CommentDTO;
import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.model.Comment;

import java.util.List;

public interface CommentService {
    CommentDTO addComment(Comment comment, Long companyId) throws Exception;

    List<CommentDTO> findAllByCompanyId(Long companyId);
}
