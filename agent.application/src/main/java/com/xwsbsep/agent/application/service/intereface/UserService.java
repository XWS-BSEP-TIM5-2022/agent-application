package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.dto.ChangePasswordDTO;
import com.xwsbsep.agent.application.dto.CompanyDTO;
import com.xwsbsep.agent.application.dto.UserDTO;
import com.xwsbsep.agent.application.model.User;

import java.util.List;

public interface UserService {
    UserDTO registerUser(User user) throws Exception;

    boolean verifyUserAccount(String verificationToken);

    List<UserDTO> getAll();

    UserDTO findByUsername(String name);

    boolean checkPasswordCriteria(String password, String username);

    CompanyDTO getCompanyByOwnerUsername(String username);

    UserDTO findById(Long id);

    void changePassword(ChangePasswordDTO dto, String name) throws Exception;

    boolean checkPasswordCriteria(String password);

    boolean checkIfEnabled2FA(String email) throws Exception;
}
