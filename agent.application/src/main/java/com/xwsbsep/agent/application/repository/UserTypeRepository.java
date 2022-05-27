package com.xwsbsep.agent.application.repository;

import com.xwsbsep.agent.application.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(String roleUser);
}
