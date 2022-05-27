package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;

public class CompanyRegistrationRequestMapper {

    public CompanyRegistrationRequestDTO mapRequestToRequestDto(CompanyRegistrationRequest request) {
        CompanyRegistrationRequestDTO dto = new CompanyRegistrationRequestDTO();
        dto.setApproved(request.isApproved());
        dto.setId(request.getId());

        //UserDTO userDTO = new UserMapper().mapUserToUserDto(request.getUser());
        CompanyDTO companyDTO = new CompanyDTO(request.getCompany().getId(), request.getCompany().getName(),
                request.getCompany().getDescription(), request.getCompany().getPhoneNumber()/*, userDTO*/);
        //userDTO.setCompanyDTO(companyDTO);
        dto.setCompanyDTO(companyDTO);
        dto.setUserId(request.getUserId());
        return dto;
    }
}
