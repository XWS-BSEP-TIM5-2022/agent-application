package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.AddCommentDTO;
import com.xwsbsep.agent.application.dto.CommentDTO;
import com.xwsbsep.agent.application.mapper.CommentMapper;
import com.xwsbsep.agent.application.model.Comment;
import com.xwsbsep.agent.application.service.intereface.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addComment(@RequestBody AddCommentDTO reqDto) throws Exception {
        try {
            Comment comment = new CommentMapper().mapAddCommentDtoToComment(reqDto);
            CommentDTO dto = commentService.addComment(comment, reqDto.getCompanyId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/company/{companyId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllByCompanyId(@PathVariable Long companyId) throws Exception {
       List<CommentDTO> commentsDto = commentService.findAllByCompanyId(companyId);
       return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }
}

