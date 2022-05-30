package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSalaryCommentDTO {
    private String position;
    private Integer pay;
    private Boolean isFormerEmployee;
    private Boolean bonus;
    private Boolean fairPay;
    private Long companyId;
}
