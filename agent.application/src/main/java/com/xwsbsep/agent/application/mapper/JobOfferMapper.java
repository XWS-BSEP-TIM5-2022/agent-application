package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.model.JobOffer;
import com.xwsbsep.agent.application.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class JobOfferMapper {

    @Autowired
    private CompanyRepository companyRepository;  // TODO: null ?

    public JobOfferDTO mapJobOfferToJobOfferDto(JobOffer jobOffer) {
        JobOfferDTO dto = new JobOfferDTO();
        dto.setId(jobOffer.getId());
        dto.setPosition(jobOffer.getPosition());
        dto.setJobDescription(jobOffer.getJobDescription());
        dto.setPreconditions(jobOffer.getPreconditions());
        dto.setDailyActivities(jobOffer.getDailyActivities());
        dto.setCompanyId(jobOffer.getCompany().getId());   // TODO
        return dto;
    }

    public JobOffer mapJobOfferDtoToJobOffer(JobOfferDTO dto) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(dto.getId());
        jobOffer.setPosition(dto.getPosition());
        jobOffer.setJobDescription(dto.getJobDescription());
        jobOffer.setPreconditions(dto.getPreconditions());
        jobOffer.setDailyActivities(dto.getDailyActivities());
       // jobOffer.setCompany(this.companyRepository.findCompanyById(dto.getCompanyId()));
//        System.out.println("---------------------");
//        System.out.println(jobOffer.getCompany());
        return jobOffer;
    }
}
