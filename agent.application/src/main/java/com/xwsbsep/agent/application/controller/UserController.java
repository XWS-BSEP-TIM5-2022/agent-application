package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.ChangePasswordDTO;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.security.util.TokenUtils;
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
    private final TokenUtils tokenUtils;
    private static final String WHITESPACE = " ";

    public UserController(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    @PreAuthorize("hasAuthority('getAllUsers')")
    public ResponseEntity<?> getAll(Principal user,@RequestHeader("Authorization") String jwtToken){
//        User user1 = userService.findByUsername(user.getName());
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        log.info("Getting all users success by user: " + username);
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    @PreAuthorize("hasAuthority('getUserByUsername')")
    public ResponseEntity<?> getByUsername(@PathVariable String username, @RequestHeader("Authorization") String jwtToken){

        log.info("User with username: " + username + " successfully found!");
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}")
    @PreAuthorize("hasAuthority('getUserById')")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestHeader("Authorization") String jwtToken){
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        log.info("User with id: " + id + " successfully found by user: " + username);
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}/company")
    @PreAuthorize("hasAuthority('getCompanyByOwnerUsername')")
    public ResponseEntity<?> getCompanyByOwnerUsername(@PathVariable String username, @RequestHeader("Authorization") String jwtToken){

        log.info("Company from owner with username: " + username + " successfully found");
        return new ResponseEntity<>(userService.getCompanyByOwnerUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/changePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('changePassword')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, Principal user, @RequestHeader("Authorization") String jwtToken) throws Exception {
        String username = tokenUtils.getUsernameFromToken(jwtToken.split(WHITESPACE)[1]);
        username = username.replaceAll("[\n\r\t]", "_");
        try {
            userService.changePassword(dto, user.getName());
            log.info("Successfully changed password by user:" + username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while changing password:" + username);

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
