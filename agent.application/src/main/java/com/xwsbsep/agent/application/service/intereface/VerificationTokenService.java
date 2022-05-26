package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.model.VerificationToken;

public interface VerificationTokenService {
    public VerificationToken saveVerificationToken(VerificationToken token);
    public VerificationToken findVerificationTokenByToken(String token);
}
