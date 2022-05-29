package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import com.xwsbsep.agent.application.service.intereface.CompanyService;
import com.xwsbsep.agent.application.service.intereface.JobOfferService;
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

    @Autowired
    private JobOfferService jobOfferService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.POST, value = "/request_registration")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveRegistrationRequest(@RequestBody CompanyRegistrationRequestDTO dto) {
        boolean saved = this.companyRegistrationRequestService.saveRegistrationRequest(dto);
        if (saved){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/approve_request/{requestId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId) {
        boolean approved = this.companyRegistrationRequestService.approveRequest(requestId);
        if (approved){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reject_request/{requestId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId) {
        boolean rejected = this.companyRegistrationRequestService.rejectRequest(requestId);
        if (rejected){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{companyId}/job_offer")
    //@PreAuthorize("hasRole('COMPANY_OWNER')")
    public ResponseEntity<?> saveJobOffer(@RequestBody JobOfferDTO dto, @PathVariable Long companyId) {
        dto.setCompanyId(companyId);
        boolean saved = this.jobOfferService.saveJobOffer(dto);
        if (saved != false){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job_offer")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAllJobOffers() {
        return new ResponseEntity(this.jobOfferService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")  // TODO: CHECK ?
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        return new ResponseEntity(this.companyService.findCompanyById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/requests")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRequests() {
        return new ResponseEntity(this.companyRegistrationRequestService.findAll(), HttpStatus.OK);
    }
}
