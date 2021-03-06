package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.controller.AuthController;
import com.xwsbsep.agent.application.dto.ChangePasswordDTO;
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
import dev.samstevens.totp.secret.SecretGenerator;
import org.apache.log4j.Logger;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private SecretGenerator secretGenerator;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");

    static Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public UserDTO  registerUser(User user) throws Exception {
        String username = user.getUsername();
        username = username.replaceAll("[\n\r\t]", "_");

        if(!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).find()){
            log.error("Registration failed. Email invalid");
            throw new Exception("Email invalid");
        }

        if(!VALID_USERNAME_REGEX.matcher(user.getUsername()).find()){
            log.error("Registration failed. Username invalid");
            throw new Exception("Username invalid");
        }
        if(!emailIsUnique(user.getEmail())){
            log.error("Registration failed. Email " + user.getEmail() + " not unique");
            throw new Exception("Email is not unique");
        }
        if(!usernameIsUnique(user.getUsername())){

            log.error("Registration failed. Username " + username + " not unique");
            throw new Exception("Username is not unique");
        }
        if (!checkPasswordCriteria(user.getPassword(), user.getUsername())) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain username and white spaces";
            log.error("Registration failed for user " + username + ". Password does not match criteria.");
            System.out.println(pswdError);
            throw new Exception(pswdError);
        }
        UserType role = userTypeService.findUserTypeByName("ROLE_USER");
        if (role == null) {
            log.error("Registration failed. There is no role with name: ROLE_USER");
            throw new Exception("Role does not exist");
        }
        user.setUserType(role);
        user.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        user.setIsActivated(false);
        user.setCompany(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.isUsing2FA()) {
            user.setSecret(secretGenerator.generate());
        }

        VerificationToken verificationToken = new VerificationToken(user);
        if (!emailService.sendAccountActivationMail(verificationToken.getToken(), user.getEmail())) {

            log.error("Registration failed. Verification email not sent to mail: " + user.getEmail());
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
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-+_!@#$%^&*.,?:;<>=`~)({}|/])(?=\\S+$).{8,}$");
        Matcher passMatcher = pattern.matcher(password);
        if(password.toLowerCase().contains(username.toLowerCase())) {
            return false;
        }
        return passMatcher.matches();
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

    @Override
    public void changePassword(ChangePasswordDTO dto, String name) throws Exception {
        String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                "letter, one lowercase letter, one number and one special character(-+_!@#$%^&*.,?:;<>=`~)({}|/) and " +
                "must not contain white spaces";
        if (!checkPasswordCriteria(dto.getNewPassword())) {
            throw new Exception(pswdError);
        }
        if (!checkPasswordCriteria(dto.getReenteredPassword())) {
            throw new Exception(pswdError);
        }

        User user = userRepository.findByUsername(name);
        if(dto.getNewPassword().toLowerCase().contains(user.getUsername().toLowerCase())) {
            throw new IllegalArgumentException(String.format("Password must not contain username"));
        }
        if(!user.getIsActivated()){
            throw new Exception("Account is not activated");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new Exception("Old password does not match the current password");
        }
        if (!dto.getNewPassword().equals(dto.getReenteredPassword())) {
            throw new Exception("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }

    @Override
    public boolean checkPasswordCriteria(String password) {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-+_!@#$%^&*.,?:;<>=`~)({}|/])(?=\\S+$).{8,}$");
        Matcher passMatcher = pattern.matcher(password);
        return passMatcher.matches();
    }

    @Override
    public boolean checkIfEnabled2FA(String username) throws Exception {

        User user = userRepository.findByUsername(username);

        if(user == null || !user.getIsActivated()){
            log.error("Check if 2FA is enabled for account failed. Account with username " + username + " not activated.");
            throw new Exception("Account with username " + username + " not activated.");
        }

        return user.isUsing2FA();
    }

}
