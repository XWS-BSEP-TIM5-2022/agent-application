package com.xwsbsep.agent.application.repository;

import com.xwsbsep.agent.application.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findCompanyById(Long id);
}
