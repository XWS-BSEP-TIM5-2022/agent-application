package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSalaryCommentDTO {
    private String position;
    private Integer pay;
    private boolean isFormerEmployee;
    private boolean bonus;
    private boolean fairPay;
    private Long companyId;
}
