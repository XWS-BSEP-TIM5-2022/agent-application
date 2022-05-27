package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.ApproveRequestDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.mapper.CompanyRegistrationRequestMapper;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.model.UserType;
import com.xwsbsep.agent.application.repository.CompanyRegistrationRequestRepository;
import com.xwsbsep.agent.application.repository.UserRepository;
import com.xwsbsep.agent.application.repository.UserTypeRepository;
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
    public CompanyRegistrationRequestDTO save(CompanyRegistrationRequest request) {
        User user = this.userRepository.findUserById(request.getUserId());
        request.getCompany().setActive(false);
        user.setCompany(request.getCompany());
        request.setApproved(false);
        this.registrationRequestRepository.save(request);
        return new CompanyRegistrationRequestMapper().mapRequestToRequestDto(request);
    }

    @Override
    public CompanyRegistrationRequestDTO approveRequest(ApproveRequestDTO dto) {
        CompanyRegistrationRequest request = this.registrationRequestRepository.findCompanyRegistrationRequestById(dto.getId());
        request.setApproved(dto.isApproved());
        request.getCompany().setActive(dto.isApproved());
        if (dto.isApproved()) {
            updateRole("ROLE_COMPANY_OWNER", request.getUserId());
        }
        this.registrationRequestRepository.save(request); // TODO ??
        return new CompanyRegistrationRequestMapper().mapRequestToRequestDto(request);
    }

    public void updateRole(String role, Long userId){
        User user = this.userRepository.findUserById(userId);
        user.setUserType(this.userTypeService.findUserTypeByName(role));
        this.userRepository.save(user);
    }
}
