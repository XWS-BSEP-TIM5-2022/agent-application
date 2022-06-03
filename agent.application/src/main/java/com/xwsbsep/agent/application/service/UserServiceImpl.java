package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.mapper.CompanyMapper;
import com.xwsbsep.agent.application.mapper.UserMapper;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.model.UserType;
import com.xwsbsep.agent.application.model.VerificationToken;
import com.xwsbsep.agent.application.repository.UserRepository;
import com.xwsbsep.agent.application.repository.VerificationTokenRepository;
import com.xwsbsep.agent.application.security.util.TokenUtils;
import com.xwsbsep.agent.application.service.intereface.UserService;
import com.xwsbsep.agent.application.service.intereface.UserTypeService;
import com.xwsbsep.agent.application.service.intereface.VerificationTokenService;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final int TOKEN_EXPIRES_MINUTES = 15;
    private final int MIN_PASSWORD_LENGTH = 8;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public UserDTO  registerUser(User user) throws Exception {
        if(!emailIsUnique(user.getEmail())){
            throw new Exception("Email is not unique");
        }
        if(!usernameIsUnique(user.getUsername())){
            throw new Exception("Username is not unique");
        }
        if (!checkPasswordCriteria(user.getPassword(), user.getUsername())) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain username and white spaces";
            System.out.println(pswdError);
            throw new Exception(pswdError);
        }
        UserType role = userTypeService.findUserTypeByName("ROLE_USER");
        if (role == null) {
            throw new Exception("Role does not exist");
        }
        user.setUserType(role);
        user.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        user.setIsActivated(false);
        user.setCompany(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        VerificationToken verificationToken = new VerificationToken(user);
        if (!emailService.sendAccountActivationMail(verificationToken.getToken(), user.getEmail())) {
            throw new Exception("Email for account verification not sent, try again");
        }
        userRepository.save(user);
        verificationTokenService.saveVerificationToken(verificationToken);
        User registeredUser = userRepository.findByEmail(user.getEmail());
        return new UserMapper().mapUserToUserDto(registeredUser);
    }

    private boolean usernameIsUnique(String username) {
        for (User user : userRepository.findAll()) {
            if(user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean emailIsUnique(String email) {
        for (User user : userRepository.findAll()) {
            if(user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean verifyUserAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        long difference_In_Time = (new Date()).getTime() - verificationToken.getCreatedDateTime().getTime();
        User user = userRepository.findByEmail(verificationToken.getUser().getEmail());
        ;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        if (difference_In_Minutes <= TOKEN_EXPIRES_MINUTES) {
            user.setIsActivated(true);
            userRepository.save(user);
            return true;
        } else {
            userRepository.delete(user);
            verificationTokenRepository.delete(verificationToken);
            return false;
        }
    }

    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            dtos.add(new UserMapper().mapUserToUserDto(user));
        }
        return dtos;
    }

    @Override
    public UserDTO findByUsername(String username) {
        return new UserMapper().mapUserToUserDto(userRepository.findByUsername(username));
    }

    @Override
    public boolean checkPasswordCriteria(String password, String username) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 100),
                new UppercaseCharacterRule(1),
                new LowercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            if(password.toLowerCase().contains(username.toLowerCase())) {
                System.out.println("Password must not contain username");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public CompanyDTO getCompanyByOwnerUsername(String username) {
        for (User user : this.userRepository.findAll()) {
            if (user.getUsername().equals(username) && user.getCompany() != null) {
                return new CompanyMapper().mapCompanyToCompanyDto(user.getCompany());
            }
        }
        return null;
    }

    @Override
    public UserDTO findById(Long id) {
        return new UserMapper().mapUserToUserDto(this.userRepository.findUserById(id));
    }

}
