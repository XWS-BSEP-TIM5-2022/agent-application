package com.xwsbsep.agent.application.repository;

import com.xwsbsep.agent.application.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
}
