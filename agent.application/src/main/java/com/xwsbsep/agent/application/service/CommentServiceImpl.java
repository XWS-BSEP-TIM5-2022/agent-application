package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.AddInterviewCommentDTO;
import com.xwsbsep.agent.application.dto.CommentDTO;
import com.xwsbsep.agent.application.dto.CommentInterviewDTO;
import com.xwsbsep.agent.application.dto.CommentSalaryDTO;
import com.xwsbsep.agent.application.mapper.CommentInterviewMapper;
import com.xwsbsep.agent.application.mapper.CommentMapper;
import com.xwsbsep.agent.application.mapper.CommentSalaryMapper;
import com.xwsbsep.agent.application.model.*;
import com.xwsbsep.agent.application.repository.CommentInterviewRepository;
import com.xwsbsep.agent.application.repository.CommentRepository;
import com.xwsbsep.agent.application.repository.CommentSalaryRepository;
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
    @Autowired
    private CommentSalaryRepository commentSalaryRepository;
    @Autowired
    private CommentInterviewRepository commentInterviewRepository;

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
            if(comment.getCompany().getId() == companyId) {
                dtos.add(new CommentMapper().mapCommentToCommentDto(comment));
            }
        }
        return dtos;
    }

    @Override
    public List<String> getPositionsByCompanyId(Long companyId) {
        List<String> positionsName = new ArrayList<>();
        Company company = companyRepository.findCompanyById(companyId);
        for(JobOffer offer: company.getJobOffers()){
            positionsName.add(offer.getPosition().getName());
        }
        return positionsName;
    }

    @Override
    public void addCommentSalary(CommentSalary comment, Long companyId) throws Exception {
        Company company = companyRepository.findCompanyById(companyId);
        if (!company.isActive())
            throw new Exception("Company is not active");
        comment.setCompany(company);
        commentSalaryRepository.save(comment);
    }

    @Override
    public List<CommentSalaryDTO> getAllSalaryCommentsByCompanyId(Long companyId) {
        List<CommentSalaryDTO> dtos = new ArrayList<>();
        for (CommentSalary comment: commentSalaryRepository.findAll()) {
            if(comment.getCompany().getId() == companyId) {
                dtos.add(new CommentSalaryMapper().mapCommentSalaryToCommentSalaryDto(comment));
            }
        }
        return dtos;
    }

    @Override
    public void addCommentInterview(AddInterviewCommentDTO reqDto) throws Exception {
        CommentInterview comment = new CommentInterviewMapper().mapAddCommentInterviewToCommentInterview(reqDto);
        Company company = companyRepository.findCompanyById(reqDto.getCompanyId());
        if(!company.isActive())
            throw new Exception("Company is not active");
        comment.setCompany(company);
        commentInterviewRepository.save(comment);
    }

    @Override
    public List<CommentInterviewDTO> getAllInterviewCommentsByCompanyId(Long companyId) {
        List<CommentInterviewDTO> dtos = new ArrayList<>();
        for (CommentInterview comment: commentInterviewRepository.findAll()) {
            if(comment.getCompany().getId() == companyId) {
                dtos.add(new CommentInterviewMapper().mapCommentInterviewToCommentInterviewDto(comment));
            }
        }
        return dtos;
    }
}
