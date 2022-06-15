package com.xwsbsep.agent.application.dto;

import com.xwsbsep.agent.application.model.Company;
import com.xwsbsep.agent.application.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Timestamp lastPasswordResetDate;
    private String firstName;
    private String lastName;
    private Boolean isActivated;
    private UserType userType;
    private Company company;
}
