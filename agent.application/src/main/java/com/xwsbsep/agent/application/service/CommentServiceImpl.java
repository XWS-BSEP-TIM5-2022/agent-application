package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.CommentDTO;
import com.xwsbsep.agent.application.mapper.CommentMapper;
import com.xwsbsep.agent.application.model.Comment;
import com.xwsbsep.agent.application.model.Company;
import com.xwsbsep.agent.application.repository.CommentRepository;
import com.xwsbsep.agent.application.repository.CompanyRepository;
import com.xwsbsep.agent.application.service.intereface.CommentService;
import com.xwsbsep.agent.application.service.intereface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDTO addComment(Comment comment, Long companyId) throws Exception {
        Company company = companyRepository.findCompanyById(companyId);
        if(!company.isActive())
            throw new Exception("Company is not active");
        comment.setCompany(company);
        commentRepository.save(comment);

        Comment savedComment = commentRepository.findByTitleLike(comment.getTitle());
        CommentDTO dto = new CommentMapper().mapCommentToCommentDto(savedComment);
        if(dto == null)
            throw new Exception("Comment is not saved, try again");
        return dto;
    }

    @Override
    public List<CommentDTO> findAllByCompanyId(Long companyId) {
        List<CommentDTO> dtos = new ArrayList<>();
        for (Comment comment: commentRepository.findAll()) {
            if(!comment.isDeleted() && comment.getCompany().getId() == companyId) {
                dtos.add(new CommentMapper().mapCommentToCommentDto(comment));
            }
        }
        return dtos;
    }
}
