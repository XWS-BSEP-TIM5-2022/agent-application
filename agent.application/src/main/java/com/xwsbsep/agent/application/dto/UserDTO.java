package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String userTypeName;
    private CompanyDTO companyDTO;
    private boolean using2FA;
    private String secret;
    private String qrCode;
}
