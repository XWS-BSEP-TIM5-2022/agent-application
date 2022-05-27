package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.ApproveRequestDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.model.CompanyRegistrationRequest;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {
    @Autowired
    private CompanyRegistrationRequestService companyRegistrationRequestService;

    @RequestMapping(method = RequestMethod.POST, value = "/request_registration")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<CompanyRegistrationRequestDTO> save(@RequestBody CompanyRegistrationRequest request) {
        CompanyRegistrationRequestDTO dto = this.companyRegistrationRequestService.save(request);
        if (dto != null){
            return new ResponseEntity(dto, HttpStatus.CREATED);
        }
        return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/approve_request")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveRequest(@RequestBody ApproveRequestDTO dto) {
        CompanyRegistrationRequestDTO request = this.companyRegistrationRequestService.approveRequest(dto);
        if (request != null){
            return new ResponseEntity(request, HttpStatus.OK);
        }

        // user menja rolu

        return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
