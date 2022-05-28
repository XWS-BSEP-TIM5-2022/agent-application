package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.mapper.CompanyMapper;
import com.xwsbsep.agent.application.mapper.CompanyRegistrationRequestMapper;
import com.xwsbsep.agent.application.model.Company;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.repository.CompanyRegistrationRequestRepository;
import com.xwsbsep.agent.application.repository.UserRepository;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import com.xwsbsep.agent.application.service.intereface.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyRegistrationRequestServiceImpl implements CompanyRegistrationRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private CompanyRegistrationRequestRepository registrationRequestRepository;

    @Override
    public boolean saveRegistrationRequest(CompanyRegistrationRequestDTO dto) {
        User user = this.userRepository.findUserById(dto.getUserId());
        if (user != null && user.getCompany() == null) {
            dto.getCompanyDTO().setActive(false);
            dto.setApproved(false);
            CompanyRegistrationRequest savedRequest = this.save(new CompanyRegistrationRequestMapper().mapRequestDtoToRequest(dto));
            user.setCompany(savedRequest.getCompany());
            this.userRepository.save(user);

            return true;
        }
        return false;
    }

    @Override
    public boolean approveRequest(Long requestId) {
        CompanyRegistrationRequest request = this.registrationRequestRepository.findCompanyRegistrationRequestById(requestId);
        if (request != null) {
            request.setApproved(true);
            request.getCompany().setActive(true);
            updateRole("ROLE_COMPANY_OWNER", request.getUserId());
            this.registrationRequestRepository.save(request);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectRequest(Long requestId) {
        CompanyRegistrationRequest request = this.registrationRequestRepository.findCompanyRegistrationRequestById(requestId);
        if (request != null && !request.isApproved()) {
            request.setApproved(false);
            request.getCompany().setActive(false);
            this.registrationRequestRepository.save(request);
            return true;
        }
        return false;
    }

    @Override
    public CompanyRegistrationRequest save(CompanyRegistrationRequest request) {
        return this.registrationRequestRepository.save(request);
    }

    public void updateRole(String role, Long userId){
        User user = this.userRepository.findUserById(userId);
        user.setUserType(this.userTypeService.findUserTypeByName(role));
        this.userRepository.save(user);
    }
}
