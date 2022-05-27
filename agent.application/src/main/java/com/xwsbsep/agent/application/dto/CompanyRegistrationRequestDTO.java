package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistrationRequestDTO {
    private Long id;
    private CompanyDTO companyDTO;
    private Long userId;
    private boolean isApproved;
}
