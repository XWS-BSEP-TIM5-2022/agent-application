package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.ApproveRequestDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;

public interface CompanyRegistrationRequestService {
    boolean saveRegistrationRequest(CompanyRegistrationRequest request);  // TODO: sa fronta dolazi DTO ??
    boolean approveRequest(Long requestId);
    boolean rejectRequest(Long requestId);
}
