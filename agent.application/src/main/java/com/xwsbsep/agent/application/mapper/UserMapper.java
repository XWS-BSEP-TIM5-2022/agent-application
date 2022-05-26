package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.model.User;

public class UserMapper {

    public UserDTO mapUserToUserDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        if(user.getUserType() != null) {
            dto.setUserTypeName(user.getUserType().getName());
        }
        if(user.getCompany() != null) {
            dto.setCompanyDTO(new CompanyMapper().mapCompanyToCompanyDto(user.getCompany()));
        }
        return dto;
    }
}
