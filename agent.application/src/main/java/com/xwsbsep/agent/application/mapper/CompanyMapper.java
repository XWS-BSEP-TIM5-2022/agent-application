package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.model.Company;

public class CompanyMapper {

    public CompanyDTO mapCompanyToCompanyDto(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        return companyDTO;
    }
}
