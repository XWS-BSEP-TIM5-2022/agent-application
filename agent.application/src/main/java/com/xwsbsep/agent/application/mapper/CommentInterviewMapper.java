package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.AddInterviewCommentDTO;
import com.xwsbsep.agent.application.dto.CommentInterviewDTO;
import com.xwsbsep.agent.application.model.CommentInterview;

public class CommentInterviewMapper {
    public CommentInterview mapAddCommentInterviewToCommentInterview(AddInterviewCommentDTO dto) {
        CommentInterview c = new CommentInterview();
        c.setPosition(dto.getPosition());
        c.setTitle(dto.getTitle());
        c.setHrInterview(dto.getHrInterview());
        c.setTechnicalInterview(dto.getTechnicalInterview());
        c.setRating(dto.getRating());
        return c;
    }

    public CommentInterviewDTO mapCommentInterviewToCommentInterviewDto(CommentInterview c) {
        CommentInterviewDTO dto = new CommentInterviewDTO();
        dto.setId(c.getId());
        dto.setPosition(c.getPosition());
        dto.setTitle(c.getTitle());
        dto.setHrInterview(c.getHrInterview());
        dto.setTechnicalInterview(c.getTechnicalInterview());
        dto.setRating(c.getRating());
        dto.setCompany(new CompanyMapper().mapCompanyToCompanyDto(c.getCompany()));
        return dto;
    }
}
