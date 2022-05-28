package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistrationRequestDTO {
    private Long id;
    private CompanyDTO companyDTO;  // ceo DTO jer pri slanju zahteva za registraciju se kreira i sama kompanija
    private Long userId;
    private boolean isApproved;
}
