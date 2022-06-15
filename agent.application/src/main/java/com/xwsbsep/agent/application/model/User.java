package com.xwsbsep.agent.application.model;

import com.xwsbsep.agent.application.dto.RegisterUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name="username", unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(name="password", nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(name="last_password_reset_date", nullable = false)
    private Timestamp lastPasswordResetDate;

    @Column(name="first_name", nullable = false)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name="last_name", nullable = false)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name="is_activated", nullable = false)
    @NotNull(message = "Is user activated is required")
    private Boolean isActivated;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "role_id")
    private UserType userType;

    @OneToOne(/*cascade = CascadeType.ALL*/)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @Column(name = "using_2fa")
    private boolean using2FA;

    private String secret;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<UserType> ut = new ArrayList<UserType>();
//        ut.add(userType);
        return this.userType.getPermissions();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActivated;
    }

    public User(RegisterUserDTO u){
        this.email = u.getEmail();
        this.company = u.getCompany();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.password = u.getPassword();
        this.username = u.getUsername();
        this.isActivated = u.getIsActivated();
        this.lastPasswordResetDate = u.getLastPasswordResetDate();
        this.userType = u.getUserType();
        this.secret = u.getSecret();
        this.using2FA = u.isUsing2FA();
    }
    public User(Long id, String email, String username, String password, Timestamp lastPasswordResetDate, String firstName, String lastName, Boolean isActivated, UserType userType, Company company) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActivated = isActivated;
        this.userType = userType;
        this.company = company;
        this.using2FA = false;
        this.secret = "";
    }
}
