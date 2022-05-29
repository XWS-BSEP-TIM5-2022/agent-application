package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.mapper.CompanyMapper;
import com.xwsbsep.agent.application.model.Company;
import com.xwsbsep.agent.application.repository.CompanyRepository;
import com.xwsbsep.agent.application.service.intereface.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public CompanyDTO findCompanyById(Long id) {
        return new CompanyMapper().mapCompanyToCompanyDto(this.companyRepository.findCompanyById(id));
    }

    @Override
    public List<CompanyDTO> findAll() {
        List<CompanyDTO> dtos = new ArrayList<>();
        for(Company c: companyRepository.findAll()){
            dtos.add(new CompanyMapper().mapCompanyToCompanyDto(c));
        }
        return dtos;
    }
}
