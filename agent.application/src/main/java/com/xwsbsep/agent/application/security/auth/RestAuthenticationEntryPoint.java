package com.xwsbsep.agent.application.security.auth;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        //Metoda koja se izvrsava ukoliko za prosledjene kredencijale korisnik pokusa da pristupi zasticenom REST servisu
        //Metoda vraca 401 Unauthorized response, ukoliko postoji Login Page u aplikaciji, pozeljno je da se korisnik redirektuje na tu stranicu
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
