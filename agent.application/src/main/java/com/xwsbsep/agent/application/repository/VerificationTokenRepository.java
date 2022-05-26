package com.xwsbsep.agent.application.repository;

import com.xwsbsep.agent.application.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findVerificationTokenByToken(String token);
}
