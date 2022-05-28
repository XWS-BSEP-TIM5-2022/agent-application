package com.xwsbsep.agent.application.mapper;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.model.Company;
import com.xwsbsep.agent.application.model.JobOffer;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

public class CompanyMapper {

    public CompanyDTO mapCompanyToCompanyDto(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setDescription(company.getDescription());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setActive(company.isActive());

//        Set<JobOfferDTO> jobOffersDTO = new HashSet<>();
//        if(company.getJobOffers() != null) {
//            for (JobOffer j : company.getJobOffers()) {
//                JobOfferDTO dto = new JobOfferMapper().mapJobOfferToJobOfferDto(j);
//                jobOffersDTO.add(dto);
//            }
//        }
//        companyDTO.setJobOffers(jobOffersDTO);

        return companyDTO;
    }
}
