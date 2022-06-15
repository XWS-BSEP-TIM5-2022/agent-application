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
import org.apache.log4j.Logger;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    static Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public UserDTO  registerUser(User user) throws Exception {
        if(!emailIsUnique(user.getEmail())){
            log.error("Registration failed. Email " + user.getEmail() + " not unique");
            throw new Exception("Email is not unique");
        }
        if(!usernameIsUnique(user.getUsername())){
            log.error("Registration failed. Username " + user.getUsername() + " not unique");
            throw new Exception("Username is not unique");
        }
        if (!checkPasswordCriteria(user.getPassword(), user.getUsername())) {
            String pswdError = "Password must contain minimum eight characters, at least one uppercase " +
                    "letter, one lowercase letter, one number and one special character and " +
                    "must not contain username and white spaces";
            log.error("Registration failed for user " + user.getUsername() + ". Password does not match criteria.");
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
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 100),
//                new UppercaseCharacterRule(1),
//                new LowercaseCharacterRule(1),
//                new DigitCharacterRule(1),
//                new SpecialCharacterRule(1),
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
//        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[-+_!@#$%^&*.,?:;<>=`~\\\\]\\x22\\x27\\(\\)\\{\\}\\|\\/\\[\\\\\\\\?]).{8,}$");
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-+_!@#$%^&*.,?:;<>=`~)({}|/])(?=\\S+$).{8,}$");
        Matcher passMatcher = pattern.matcher(password);
        System.out.println(passMatcher.matches());
        return passMatcher.matches() /*&& result.isValid()*/;
    }

}
