package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.*;
import com.xwsbsep.agent.application.mapper.CommentMapper;
import com.xwsbsep.agent.application.mapper.CommentSalaryMapper;
import com.xwsbsep.agent.application.model.Comment;
import com.xwsbsep.agent.application.model.CommentSalary;
import com.xwsbsep.agent.application.service.intereface.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addCompanyComment(@RequestBody AddCommentDTO reqDto) throws Exception {
        try {
            Comment comment = new CommentMapper().mapAddCommentDtoToComment(reqDto);
            CommentDTO dto = commentService.addComment(comment, reqDto.getCompanyId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllCompanyCommentsByCompanyId(@PathVariable Long companyId) throws Exception {
       List<CommentDTO> commentsDto = commentService.findAllByCompanyId(companyId);
       return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}/positions")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPositionsByCompanyId(@PathVariable Long companyId) throws Exception {
        List<String> positionsName = commentService.getPositionsByCompanyId(companyId);
        return new ResponseEntity<>(positionsName, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST,value = "/salary" )
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addSalaryComment(@RequestBody AddSalaryCommentDTO reqDto) throws Exception {
        try {
            CommentSalary comment = new CommentSalaryMapper().mapAddCommentDtoToComment(reqDto);
            commentService.addCommentSalary(comment, reqDto.getCompanyId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/salary/company/{companyId}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllSalaryCommentsByCompanyId(@PathVariable Long companyId) throws Exception {
        List<CommentSalaryDTO> commentsDto = commentService.getAllSalaryCommentsByCompanyId(companyId);
        return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/interview" )
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addInterviewComment(@RequestBody AddInterviewCommentDTO reqDto) throws Exception {
        try {
            commentService.addCommentInterview(reqDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/interview/company/{companyId}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllInterviewCommentsByCompanyId(@PathVariable Long companyId) throws Exception {
        List<CommentInterviewDTO> commentsDto = commentService.getAllInterviewCommentsByCompanyId(companyId);
        return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }
}

