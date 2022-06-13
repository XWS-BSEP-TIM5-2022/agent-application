package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.JwtAuthenticationDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.dto.UserTokenStateDTO;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.security.util.TokenUtils;
import com.xwsbsep.agent.application.service.intereface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;

    static Logger log = Logger.getLogger(AuthController.class.getName());


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) throws Exception {

        try {
            UserDTO userDTO = userService.registerUser(user);
            if(userDTO == null) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Successful registration with email: " + user.getEmail());

            return new ResponseEntity(userDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("token")String verificationToken) {
        if(userService.verifyUserAccount(verificationToken)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Valid JwtAuthenticationDTO authenticationRequest, HttpServletRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (Exception ex) {
            if (ex.getMessage().contains("User is disabled")) {

                log.error("Failed login. User email: " + authenticationRequest.getEmail() + " , Ip address: " + request.getRemoteAddr() + " . Account not activated.");
                return new ResponseEntity("Account is not activated", HttpStatus.BAD_REQUEST);
            }

            log.warn("Failed login. User email: " + authenticationRequest.getEmail() + " , Ip address: " + request.getRemoteAddr() + " . Bad credentials.");
            return new ResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.getIsActivated()) {

            log.error("Failed login. User email: " + authenticationRequest.getEmail() + " , Ip address: " + request.getRemoteAddr() + " . Account not activated.");
            return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
        }
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().getName());
        int expiresIn = tokenUtils.getExpiredIn();

        log.info("Successful login with email: " + authenticationRequest.getEmail() + " from ip address: " + request.getRemoteAddr());

        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

}
