package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.model.JobOffer;

import java.util.List;

public interface JobOfferService {
    boolean saveJobOffer(JobOfferDTO dto);
    List<JobOfferDTO> getAll();
}
