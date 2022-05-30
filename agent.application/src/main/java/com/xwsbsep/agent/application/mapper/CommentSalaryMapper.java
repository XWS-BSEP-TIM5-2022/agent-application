package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.AddSalaryCommentDTO;
import com.xwsbsep.agent.application.dto.CommentSalaryDTO;
import com.xwsbsep.agent.application.model.Comment;
import com.xwsbsep.agent.application.model.CommentSalary;

public class CommentSalaryMapper {
    public CommentSalary mapAddCommentDtoToComment(AddSalaryCommentDTO dto) {
        CommentSalary c = new CommentSalary();
        c.setPosition(dto.getPosition());
        c.setPay(dto.getPay());
        c.setIsFormerEmployee(dto.getIsFormerEmployee());
        c.setBonus(dto.getBonus());
        c.setFairPay(dto.getFairPay());
        return c;
    }

    public CommentSalaryDTO mapCommentSalaryToCommentSalaryDto(CommentSalary comment) {
        CommentSalaryDTO c = new CommentSalaryDTO();
        c.setId(comment.getId());
        c.setPosition(comment.getPosition());
        c.setPay(comment.getPay());
        c.setFormerEmployee(comment.getIsFormerEmployee());
        c.setBonus(comment.getBonus());
        c.setFairPay(comment.getFairPay());
        c.setCompanyDTO(new CompanyMapper().mapCompanyToCompanyDto(comment.getCompany()));
        return c;
    }
}
