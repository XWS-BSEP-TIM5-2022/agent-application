package com.xwsbsep.agent.application.repository;

import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRegistrationRequestRepository extends JpaRepository<CompanyRegistrationRequest, Long> {
    CompanyRegistrationRequest findCompanyRegistrationRequestById(Long id);
}
