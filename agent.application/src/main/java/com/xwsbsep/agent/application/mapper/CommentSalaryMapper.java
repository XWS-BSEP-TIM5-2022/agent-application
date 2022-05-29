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
        c.setFormerEmployee(dto.isFormerEmployee());
        c.setBonus(dto.isBonus());
        c.setFairPay(dto.isFairPay());
        return c;
    }

    public CommentSalaryDTO mapCommentSalaryToCommentSalaryDto(CommentSalary comment) {
        CommentSalaryDTO c = new CommentSalaryDTO();
        c.setId(comment.getId());
        c.setPosition(comment.getPosition());
        c.setPay(comment.getPay());
        c.setFormerEmployee(comment.isFormerEmployee());
        c.setBonus(comment.isBonus());
        c.setFairPay(comment.isFairPay());
        c.setCompanyDTO(new CompanyMapper().mapCompanyToCompanyDto(comment.getCompany()));
        return c;
    }
}
