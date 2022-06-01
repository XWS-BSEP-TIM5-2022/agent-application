package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.model.Company;

import java.util.List;

public interface CompanyService {
    CompanyDTO findCompanyById(Long id);

    List<CompanyDTO> findAll();
    boolean updateCompanyInfo(CompanyDTO dto);
}
