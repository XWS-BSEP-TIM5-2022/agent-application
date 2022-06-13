package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.CommentDTO;
import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.model.Comment;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import com.xwsbsep.agent.application.service.intereface.CommentService;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import com.xwsbsep.agent.application.service.intereface.CompanyService;
import com.xwsbsep.agent.application.service.intereface.JobOfferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyRegistrationRequestService companyRegistrationRequestService;

    @Autowired
    private JobOfferService jobOfferService;

    @Autowired
    private CompanyService companyService;

    static Logger log = Logger.getLogger(CompanyController.class.getName());


    @RequestMapping(method = RequestMethod.POST, value = "/request_registration")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveRegistrationRequest(@Valid @RequestBody CompanyRegistrationRequestDTO dto) {
        boolean saved = this.companyRegistrationRequestService.saveRegistrationRequest(dto);
        if (saved){
            log.info("Company: " + dto.getCompanyDTO().getName() + " successfully activated");
            //log.info("Company" + dto.getCompanyDTO().getName() + "successfully activated by" + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/approve_request/{requestId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId) {
        boolean approved = this.companyRegistrationRequestService.approveRequest(requestId);
        if (approved){
            log.info("Company with id: " + requestId + " successfully activated");
            return new ResponseEntity(HttpStatus.OK);
        }
        log.error("Error while approving request for registration company with id: " + requestId);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reject_request/{requestId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId) {
        boolean rejected = this.companyRegistrationRequestService.rejectRequest(requestId);
        if (rejected){
            log.info("Request for company with id: " + requestId + " successfully rejected");
            return new ResponseEntity(HttpStatus.OK);
        }
        log.error("Error while rejecting request for registration company with id: " + requestId);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{companyId}/job_offer")
    //@PreAuthorize("hasRole('COMPANY_OWNER')")
    public ResponseEntity<?> saveJobOffer(@Valid @RequestBody JobOfferDTO dto, @PathVariable Long companyId) {
        dto.setCompanyId(companyId);
        boolean saved = this.jobOfferService.saveJobOffer(dto);
        if (saved != false){
            log.info("Job offer with id: " + dto.getId() + " successfully saved to the company with id: " + companyId);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        log.error("Error while saving job offer with id: " + dto.getId() + " to the company with id: " + companyId);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job_offer")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAllJobOffers() {
        log.info("Getting all jobs offers success!");
        return new ResponseEntity(this.jobOfferService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")  // TODO: CHECK ?
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        log.info("Getting company with id: " + id + " success!");
        return new ResponseEntity(this.companyService.findCompanyById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/requests")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRequests() {
        log.info("Getting all requests success!");
        return new ResponseEntity(this.companyRegistrationRequestService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')") å
    public ResponseEntity<?> getAll() {
        log.info("Getting all companies success!");
        return new ResponseEntity(this.companyService.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/updateCompanyInfo")
    //@PreAuthorize("hasRole('COMPANY_OWNER')")
    public ResponseEntity<?> updateCompanyInfo(@RequestBody CompanyDTO dto) {
        boolean updated = this.companyService.updateCompanyInfo(dto);
        if (updated){
            log.info("Company with id: " + dto.getId() + " successfully updated!");
            return new ResponseEntity(HttpStatus.OK);
        }
        log.error("Company update failed. Id company is: " + dto.getId());
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
