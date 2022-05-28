package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentDTO {
    private String title;
    private String content;
    private Long companyId;
}

