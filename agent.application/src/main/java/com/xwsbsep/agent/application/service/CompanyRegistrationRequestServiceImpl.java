package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.mapper.CompanyRegistrationRequestMapper;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.repository.CompanyRegistrationRequestRepository;
import com.xwsbsep.agent.application.repository.UserRepository;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyRegistrationRequestServiceImpl implements CompanyRegistrationRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRegistrationRequestRepository registrationRequestRepository;

    @Override
    public CompanyRegistrationRequestDTO save(CompanyRegistrationRequest request) {
        User user = this.userRepository.findUserById(request.getUserId());
        user.setCompany(request.getCompany());
        request.setApproved(false);
        this.registrationRequestRepository.save(request);
        return new CompanyRegistrationRequestMapper().mapRequestToRequestDto(request);
    }

    @Override
    public boolean approveRequest(CompanyRegistrationRequestDTO dto) {
        return false;
    }
}
