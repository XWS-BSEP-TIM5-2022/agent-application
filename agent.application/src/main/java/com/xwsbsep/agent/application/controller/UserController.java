package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.service.intereface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(Principal user){
//        User user1 = userService.findByUsername(user.getName());
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    //@PreAuthorize("hasRole('COMPANY_OWNER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}/company")
    //@PreAuthorize("hasRole('COMPANY_OWNER')")
    public ResponseEntity<?> getCompanyByOwnerUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.getCompanyByOwnerUsername(username), HttpStatus.OK);
    }

}
