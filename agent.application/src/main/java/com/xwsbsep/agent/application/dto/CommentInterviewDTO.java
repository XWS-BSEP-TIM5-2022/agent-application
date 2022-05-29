package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInterviewDTO {
    private Long id;
    private String position;
    private String title;
    private String hrInterview;
    private String technicalInterview;
    private Integer rating;
    private CompanyDTO company;
}
