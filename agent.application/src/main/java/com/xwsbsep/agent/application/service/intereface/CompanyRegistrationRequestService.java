package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;

public interface CompanyRegistrationRequestService {
    CompanyRegistrationRequestDTO save(CompanyRegistrationRequest request);
    boolean approveRequest(CompanyRegistrationRequestDTO dto);
}
