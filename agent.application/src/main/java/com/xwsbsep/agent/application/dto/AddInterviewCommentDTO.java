package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInterviewCommentDTO {
    private String position;
    private String title;
    private String hrInterview;
    private String technicalInterview;
    private Integer rating;
    private Long companyId;
}
