package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;

public interface CompanyRegistrationRequestService {
    boolean saveRegistrationRequest(CompanyRegistrationRequestDTO dto);
    boolean approveRequest(Long requestId);
    boolean rejectRequest(Long requestId);
    CompanyRegistrationRequest save(CompanyRegistrationRequest request);
}