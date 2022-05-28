package com.xwsbsep.agent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private boolean isActive;
    //private Set<JobOfferDTO> jobOffers;
}
