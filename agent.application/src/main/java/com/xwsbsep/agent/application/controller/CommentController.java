package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.*;
import com.xwsbsep.agent.application.mapper.CommentSalaryMapper;
import com.xwsbsep.agent.application.model.CommentSalary;
import com.xwsbsep.agent.application.security.util.TokenUtils;
import com.xwsbsep.agent.application.service.intereface.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List; 

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    private final TokenUtils tokenUtils;
    private static final String WHITESPACE = " ";
    
    static Logger log = Logger.getLogger(AuthController.class.getName());

    public CommentController(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }


    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('addCompanyComment')")
    public ResponseEntity<?> addCompanyComment(@RequestBody @Valid AddCommentDTO dto, @RequestHeader("Authorization") String jwtToken) throws Exception {

        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        try {
            commentService.addComment(dto);
            log.info("Company with id: " + dto.getCompanyId() + " successfully commented by user: " + username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unsuccessful commenting by user: " +  username + ".Company with id: " + dto.getCompanyId() + " is not active.");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}")
    @PreAuthorize("hasAuthority('getAllCompanyCommentsByCompanyId')")
    public ResponseEntity<?> getAllCompanyCommentsByCompanyId(@PathVariable Long companyId) throws Exception {
       List<CommentDTO> commentsDto = commentService.findAllByCompanyId(companyId);
       log.info("Found " + commentsDto.size() + " comments for company with id " + companyId);
       return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}/positions")
    @PreAuthorize("hasAuthority('getPositionsByCompanyId')")
    public ResponseEntity<?> getPositionsByCompanyId(@PathVariable Long companyId) throws Exception {
        List<String> positionsName = commentService.getPositionsByCompanyId(companyId);
        log.info("Found " + positionsName.size() + " positions for company with id " + companyId);
        return new ResponseEntity<>(positionsName, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST,value = "/salary" )
    @PreAuthorize("hasAuthority('addSalaryComment')")
    public ResponseEntity<?> addSalaryComment(@RequestBody @Valid AddSalaryCommentDTO reqDto, @RequestHeader("Authorization") String jwtToken) throws Exception {

        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);

        try {
            CommentSalary comment = new CommentSalaryMapper().mapAddCommentDtoToComment(reqDto);
            commentService.addCommentSalary(comment, reqDto.getCompanyId());
            log.info("Salary for company with id: " + reqDto.getCompanyId() + " successfully commented by user: " + username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unsuccessful commenting salary by user: " +  username + ".Company with id: " + reqDto.getCompanyId() + " is not active.");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/salary/company/{companyId}")
    @PreAuthorize("hasAuthority('getAllSalaryCommentsByCompanyId')")
    public ResponseEntity<?> getAllSalaryCommentsByCompanyId(@PathVariable Long companyId) throws Exception {
        List<CommentSalaryDTO> commentsDto = commentService.getAllSalaryCommentsByCompanyId(companyId);
        log.info("Found " + commentsDto.size() + " comments for salary in company with id " + companyId);
        return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/interview" )
    @PreAuthorize("hasAuthority('addInterviewComment')")
    public ResponseEntity<?> addInterviewComment(@RequestBody @Valid AddInterviewCommentDTO reqDto, @RequestHeader("Authorization") String jwtToken) throws Exception {

        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        try {
            commentService.addCommentInterview(reqDto);
            log.info("Interview for company with id: " + reqDto.getCompanyId() + " successfully commented by user: " + username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unsuccessful commenting interview by user: " +  username + ".Company with id: " + reqDto.getCompanyId() + " is not active.");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/interview/company/{companyId}")
    @PreAuthorize("hasAuthority('getAllInterviewCommentsByCompanyId')")
    public ResponseEntity<?> getAllInterviewCommentsByCompanyId(@PathVariable Long companyId) throws Exception {
        List<CommentInterviewDTO> commentsDto = commentService.getAllInterviewCommentsByCompanyId(companyId);
        log.info("Found " + commentsDto.size() + " comments for interviews in company with id " + companyId);
        return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }
}

