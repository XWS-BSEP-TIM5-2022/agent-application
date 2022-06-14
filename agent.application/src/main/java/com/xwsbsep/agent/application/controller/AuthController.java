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

    private static final String WHITESPACE = " ";


    static Logger log = Logger.getLogger(AuthController.class.getName());


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) throws Exception {
        System.out.println("cao");
        try {
            UserDTO userDTO = userService.registerUser(user);
            if(userDTO == null) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Successful registration with email: " + user.getEmail() + ". User id: " + userDTO.getId());

            return new ResponseEntity(userDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Registration failed for user with username: " + user.getUsername());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("token")String verificationToken, HttpServletRequest request) {

        if(userService.verifyUserAccount(verificationToken)) {

            String username = tokenUtils.getUsernameFromToken(verificationToken.split(WHITESPACE)[1]);
            log.info("Successfully activated account by user " + username);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        log.warn("Tried account activation with invalid token: " + verificationToken + " From ip address: " + request.getRemoteAddr());
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

                log.error("Failed login. Username: " + authenticationRequest.getEmail() + " , ip address: " + request.getRemoteAddr() + " . Account not activated.");
                return new ResponseEntity("Account is not activated", HttpStatus.BAD_REQUEST);
            }

            log.warn("Failed login. Username: " + authenticationRequest.getEmail() + " , ip address: " + request.getRemoteAddr() + " . Bad credentials.");
            return new ResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.getIsActivated()) {

            log.error("Failed login. Username: " + authenticationRequest.getEmail() + " , ip address: " + request.getRemoteAddr() + " . Account not activated.");
            return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
        }
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().getName());
        int expiresIn = tokenUtils.getExpiredIn();

        log.info("Successful login. Username: " + authenticationRequest.getEmail() + " , ip address: " + request.getRemoteAddr());

        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

}
