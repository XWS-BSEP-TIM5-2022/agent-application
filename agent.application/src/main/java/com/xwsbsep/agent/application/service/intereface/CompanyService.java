package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.model.Company;

public interface CompanyService {
    CompanyDTO findCompanyById(Long id);
}
