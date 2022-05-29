package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSalaryDTO {
    private Long id;
    private String position;
    private Integer pay;
    private boolean isFormerEmployee;
    private boolean bonus;
    private boolean fairPay;
    private CompanyDTO companyDTO;
}
