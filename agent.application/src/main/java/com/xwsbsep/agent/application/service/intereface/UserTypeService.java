package com.xwsbsep.agent.application.service.intereface;

import com.xwsbsep.agent.application.model.UserType;

public interface UserTypeService {
    UserType findUserTypeByName(String role_user);
}
