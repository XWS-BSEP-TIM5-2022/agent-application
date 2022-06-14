package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.ChangePasswordDTO;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.service.intereface.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;
    static Logger log = Logger.getLogger(UserController.class.getName());

    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(Principal user){
//        User user1 = userService.findByUsername(user.getName());
        log.info("Getting all users success!");
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    @PreAuthorize("hasAuthority('getUserByUsername')")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getByUsername(@PathVariable String username){
        log.info("User with username: " + username + " successfully found");
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getById(@PathVariable Long id){
        log.info("User with id: " + id + " successfully found");
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}/company")
    //@PreAuthorize("hasRole('COMPANY_OWNER')")
    public ResponseEntity<?> getCompanyByOwnerUsername(@PathVariable String username){
        log.info("Company from owner with username: " + username + " successfully found");
        return new ResponseEntity<>(userService.getCompanyByOwnerUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/changePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAuthority('changePassword')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, Principal user) throws Exception {
        try {
            userService.changePassword(dto, user.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
