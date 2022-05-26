package com.xwsbsep.agent.application.dto;

// DTO za login
public class JwtAuthenticationDTO {
    private String email;
    private String password;

    public JwtAuthenticationDTO() {
        super();
    }

    public JwtAuthenticationDTO(String username, String password) {
        this.setEmail(username);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
