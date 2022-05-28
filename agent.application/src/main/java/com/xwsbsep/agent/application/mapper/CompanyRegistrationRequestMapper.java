package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.model.Company;
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

    public CompanyRegistrationRequest mapRequestDtoToRequest(CompanyRegistrationRequestDTO dto) {
        CompanyRegistrationRequest request = new CompanyRegistrationRequest();
        request.setApproved(request.isApproved());
        request.setId(request.getId());
        Company company = new CompanyMapper().mapCompanyDtoToCompany(dto.getCompanyDTO());
        request.setCompany(company);
        request.setUserId(dto.getUserId());
        return request;
    }
}
