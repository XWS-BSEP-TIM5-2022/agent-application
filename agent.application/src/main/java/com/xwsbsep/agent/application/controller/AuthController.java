package com.xwsbsep.agent.application.controller;

import com.xwsbsep.agent.application.dto.JwtAuthenticationDTO;
import com.xwsbsep.agent.application.dto.RegisterUserDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.dto.UserTokenStateDTO;
import com.xwsbsep.agent.application.model.Permission;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.repository.VerificationTokenRepository;
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
import java.util.Collection;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    static Logger log = Logger.getLogger(AuthController.class.getName());


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterUserDTO u) throws Exception {
        User user = new User(u);
        try {
            UserDTO userDTO = userService.registerUser(user);
            if(userDTO == null) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Successful registration with email: " + user.getEmail() + ". User id: " + userDTO.getId());

            return new ResponseEntity(userDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            if(!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).find()){
                log.error("Registration failed for user - email invalid");

            }else{
                log.error("Registration failed for user with email: " + user.getEmail());
            }
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("token")String verificationToken, HttpServletRequest request) {

        if(userService.verifyUserAccount(verificationToken)) {
            String email = verificationTokenRepository.findVerificationTokenByToken(verificationToken).getUser().getEmail();
            if(!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
                log.error("Tried account activation with invalid token. From ip address: " + request.getRemoteAddr());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Successfully activated account by user with email: " + email);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        verificationToken = verificationToken.replaceAll("[\n\r\t]", "_");
        log.warn("Tried account activation with invalid token: " + verificationToken + " From ip address: " + request.getRemoteAddr());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Valid JwtAuthenticationDTO authenticationRequest, HttpServletRequest request) {
        Authentication authentication;
        String email = authenticationRequest.getEmail();
        email = email.replaceAll("[\n\r\t]", "_");

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (Exception ex) {
            if (ex.getMessage().contains("User is disabled")) {

                log.error("Failed login. Username: " + email + " , ip address: " + request.getRemoteAddr() + " . Account not activated.");
                return new ResponseEntity("Account is not activated", HttpStatus.BAD_REQUEST);
            }

            log.warn("Failed login. Username: " + email + " , ip address: " + request.getRemoteAddr() + " . Bad credentials.");
            return new ResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.getIsActivated()) {

            log.error("Failed login. Username: " + email+ " , ip address: " + request.getRemoteAddr() + " . Account not activated.");
            return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
        }
        Collection<Permission> p = user.getUserType().getPermissions();
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().getName(), p);
        int expiresIn = tokenUtils.getExpiredIn();

        log.info("Successful login. Username: " + email + " , ip address: " + request.getRemoteAddr());

        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

}
