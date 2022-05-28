package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;

public class CompanyRegistrationRequestMapper {

    public CompanyRegistrationRequestDTO mapRequestToRequestDto(CompanyRegistrationRequest request) {
        CompanyRegistrationRequestDTO dto = new CompanyRegistrationRequestDTO();
        dto.setApproved(request.isApproved());
        dto.setId(request.getId());
        CompanyDTO companyDTO = new CompanyMapper().mapCompanyToCompanyDto(request.getCompany());
        dto.setCompanyDTO(companyDTO);
        dto.setUserId(request.getUserId());
        return dto;
    }
}
