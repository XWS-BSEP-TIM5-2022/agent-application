package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.AddCommentDTO;
import com.xwsbsep.agent.application.dto.CommentDTO;
import com.xwsbsep.agent.application.model.Comment;

public class CommentMapper {

    public CommentDTO mapCommentToCommentDto(Comment c) {
        CommentDTO dto = new CommentDTO();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setContent(c.getContent());
        if(c.getCompany() != null) {
            dto.setCompanyDTO(new CompanyMapper().mapCompanyToCompanyDto(c.getCompany()));
        }
        return dto;
    }

    public Comment mapAddCommentDtoToComment(AddCommentDTO dto) {
        Comment c = new Comment();
        c.setTitle(dto.getTitle());
        c.setContent(dto.getContent());
        c.setDeleted(false);
        c.setApproved(true);
        return c;
    }
}
