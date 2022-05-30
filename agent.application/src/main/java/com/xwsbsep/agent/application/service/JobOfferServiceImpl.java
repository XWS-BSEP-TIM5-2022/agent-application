package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.mapper.JobOfferMapper;
import com.xwsbsep.agent.application.model.Company;
import com.xwsbsep.agent.application.model.JobOffer;
import com.xwsbsep.agent.application.repository.CompanyRepository;
import com.xwsbsep.agent.application.repository.JobOfferRepository;
import com.xwsbsep.agent.application.service.intereface.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public boolean saveJobOffer(JobOfferDTO dto) {
        Company company = this.companyRepository.findCompanyById(dto.getCompanyId());
        if (company != null && company.isActive()) {
            JobOffer jobOffer = new JobOfferMapper().mapJobOfferDtoToJobOffer(dto);
            jobOffer.setCompany(company);
            this.jobOfferRepository.save(jobOffer);
            return true;
        }
        return false;
    }

    @Override
    public List<JobOfferDTO> getAll() {
        List<JobOfferDTO> dtos = new ArrayList<>();
        for (JobOffer j: this.jobOfferRepository.findAll()) {
            JobOfferDTO dto = new JobOfferMapper().mapJobOfferToJobOfferDto(j);
            dtos.add(dto);
        }
        return dtos;
    }
}
