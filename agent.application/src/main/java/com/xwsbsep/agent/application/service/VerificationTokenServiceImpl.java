package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.model.VerificationToken;
import com.xwsbsep.agent.application.repository.VerificationTokenRepository;
import com.xwsbsep.agent.application.service.intereface.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl  implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken saveVerificationToken(VerificationToken token) {
        return verificationTokenRepository.save(token);
    }

    @Override
    public VerificationToken findVerificationTokenByToken(String token) {
        return verificationTokenRepository.findVerificationTokenByToken(token);
    }

}

