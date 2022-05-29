package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String title;
    private String content;
    private Integer rating;
    private String position;
    private CompanyDTO companyDTO;
}
