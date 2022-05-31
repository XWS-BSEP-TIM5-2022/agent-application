package com.xwsbsep.agent.application;

import com.xwsbsep.agent.application.model.User;
import com.xwsbsep.agent.application.model.UserType;
import com.xwsbsep.agent.application.repository.UserRepository;
import com.xwsbsep.agent.application.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private UserTypeRepository userTypeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		insert();
	}

	private void insert() {
		UserType userRole = new UserType();
		userRole.setName("ROLE_USER");
		userTypeRepository.save(userRole);

		UserType adminRole = new UserType();
		adminRole.setName("ROLE_ADMIN");
		userTypeRepository.save(adminRole);

		UserType companyOwnerRole = new UserType();
		companyOwnerRole.setName("ROLE_COMPANY_OWNER");
		userTypeRepository.save(companyOwnerRole);

		User admin = new User(1L, "sanja.drinic776@gmail.com", "sanja", passwordEncoder.encode("sanja"), Timestamp.from(Instant.now()), "Sanja","Drinic", true, adminRole, null);
		userRepository.save(admin);  
	}
}
