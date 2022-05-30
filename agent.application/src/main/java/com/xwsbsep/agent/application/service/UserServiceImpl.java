package com.xwsbsep.agent.application.service;

import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.mapper.CompanyMapper;
import com.xwsbsep.agent.application.mapper.UserMapper;
import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.model.VerificationToken;
import com.xwsbsep.agent.application.repository.UserRepository;
import com.xwsbsep.agent.application.repository.VerificationTokenRepository;
import com.xwsbsep.agent.application.security.util.TokenUtils;
import com.xwsbsep.agent.application.service.intereface.UserService;
import com.xwsbsep.agent.application.service.intereface.UserTypeService;
import com.xwsbsep.agent.application.service.intereface.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public UserDTO registerUser(User user) {
        user.setUserType(userTypeService.findUserTypeByName("ROLE_USER"));
        user.setLastPasswordResetDate(Timestamp.from(Instant.now()));
        user.setActivated(false);
        user.setCompany(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        VerificationToken verificationToken = new VerificationToken(user);
        userRepository.save(user);
        verificationTokenService.saveVerificationToken(verificationToken);
        if(!emailService.sendAccountActivationMail(verificationToken.getToken(), user.getEmail())){
            return null;
        }
        User registeredUser = userRepository.findByEmail(user.getEmail());
        return new UserMapper().mapUserToUserDto(registeredUser);
    }

    @Override
    public boolean verifyUserAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        long difference_In_Time = (new Date()).getTime() - verificationToken.getCreatedDateTime().getTime();
        User user = userRepository.findByEmail(verificationToken.getUser().getEmail());;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        if(difference_In_Minutes <= TOKEN_EXPIRES_MINUTES) {
            user.setActivated(true);
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
        for(User user: userRepository.findAll()) {
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
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{" + MIN_PASSWORD_LENGTH + ",}$";
        if (!password.matches(pattern))
            return password.matches(pattern);
        if (password.toLowerCase().contains(username.toLowerCase())) {
            return false;
        }
        return true;
    }

    @Override
    public CompanyDTO getCompanyByOwnerUsername(String username) {
        for (User user: this.userRepository.findAll()) {
            if (user.getUsername().equals(username) && user.getCompany() != null){
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
