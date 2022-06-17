package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.CompanyRegistrationRequestDTO;
import com.xwsbsep.agent.application.dto.JobOfferDTO;
import com.xwsbsep.agent.application.security.util.TokenUtils;
import com.xwsbsep.agent.application.service.intereface.CompanyRegistrationRequestService;
import com.xwsbsep.agent.application.service.intereface.CompanyService;
import com.xwsbsep.agent.application.service.intereface.JobOfferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final TokenUtils tokenUtils;
    private static final String WHITESPACE = " ";

    public CompanyController(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/request_registration")
    @PreAuthorize("hasAuthority('saveRegistrationRequest')")
    public ResponseEntity<?> saveRegistrationRequest(@Valid @RequestBody CompanyRegistrationRequestDTO dto, @RequestHeader("Authorization") String jwtToken) {
        boolean saved = this.companyRegistrationRequestService.saveRegistrationRequest(dto);
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");

        if (saved){
            log.info("Request for company: " + dto.getCompanyDTO().getName() + " successfully saved by user: " + username);
            //log.info("Company" + dto.getCompanyDTO().getName() + "successfully activated by" + SecurityContextHolder.getContext().getAuthentication().getName());
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/approve_request/{requestId}")
    @PreAuthorize("hasAuthority('approveRequest')")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId, @RequestHeader("Authorization") String jwtToken) {
        boolean approved = this.companyRegistrationRequestService.approveRequest(requestId);
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");

        if (approved){
            log.info("Company with id: " + requestId + " successfully activated by user: " + username);
            log.info("User become COMPANY OWNER!");
            return new ResponseEntity(HttpStatus.OK);
        }
        log.error("Error while approving request for registration company with id: " + requestId);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reject_request/{requestId}")
    @PreAuthorize("hasAuthority('rejectRequest')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId,  @RequestHeader("Authorization") String jwtToken) {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");

        boolean rejected = this.companyRegistrationRequestService.rejectRequest(requestId);
        if (rejected){
            log.info("Request for company with id: " + requestId + " successfully rejected by user: " + username);
            return new ResponseEntity(HttpStatus.OK);
        }
        log.error("Error while rejecting request for registration company with id: " + requestId);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{companyId}/job_offer")
    @PreAuthorize("hasAuthority('saveJobOffer')")
    public ResponseEntity<?> saveJobOffer(@Valid @RequestBody JobOfferDTO dto, @PathVariable Long companyId,  @RequestHeader("Authorization") String jwtToken) {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        dto.setCompanyId(companyId);
        boolean saved = this.jobOfferService.saveJobOffer(dto);
        if (saved != false){
            log.info("Job offer with id: " + dto.getId() + " successfully saved to the company with id: " + companyId + " by user: " + username);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        log.error("Error while saving job offer with id: " + dto.getId() + " to the company with id: " + companyId + " by user: " + username);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job_offer")
    @PreAuthorize("hasAuthority('getAllJobOffers')")
    public ResponseEntity<?> getAllJobOffers( @RequestHeader("Authorization") String jwtToken) {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        log.info("Getting all jobs offers success by user: " + username);
        return new ResponseEntity(this.jobOfferService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @PreAuthorize("hasAuthority('getCompanyById')")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id,  @RequestHeader("Authorization") String jwtToken) {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        log.info("Getting company with id: " + id + " success by user:" + username);
        return new ResponseEntity(this.companyService.findCompanyById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/requests")
    @PreAuthorize("hasAuthority('getAllRequests')")
    public ResponseEntity<?> getAllRequests( @RequestHeader("Authorization") String jwtToken) {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        log.info("Getting all requests success by user:" + username);
        return new ResponseEntity(this.companyRegistrationRequestService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('getAllCompanies')")
    public ResponseEntity<?> getAll( @RequestHeader("Authorization") String jwtToken) {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        log.info("Getting all companies success by user:" + username);
        return new ResponseEntity(this.companyService.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/updateCompanyInfo")
    @PreAuthorize("hasAuthority('updateCompanyInfo')")
    public ResponseEntity<?> updateCompanyInfo(@RequestBody CompanyDTO dto, @RequestHeader("Authorization") String jwtToken) {
        boolean updated = this.companyService.updateCompanyInfo(dto);
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        if (updated){
            log.info("Company with id: " + dto.getId() + " successfully updated by user:" + username);
            return new ResponseEntity(HttpStatus.OK);
        }
        log.error("Company update failed. Id company is: " + dto.getId() + " by user:" + username);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
