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

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        if(!userService.checkPasswordCriteria(user.getPassword(), user.getUsername())) {
            return new ResponseEntity("Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserDTO userDTO = userService.registerUser(user);
        if(userDTO == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(userDTO, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("token")String verificationToken) {
        if(userService.verifyUserAccount(verificationToken)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody JwtAuthenticationDTO authenticationRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (Exception ex) {
            return new ResponseEntity("Auth error", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.isActivated()) {
            return new ResponseEntity("User is not activated", HttpStatus.BAD_REQUEST);
        }
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().getName());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

}
