package com.xwsbsep.agent.application;

import com.xwsbsep.agent.application.model.Permission;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
		/* ADMIN */
		Set<Permission> adminPermissions = getAdminPermissions();
		/* USER */
		Set<Permission> userPermissions = getUserPermissions();

		Set<Permission> companyOwnerPermissions = getCompanyOwnerPermissions();

		UserType userRole = new UserType();
		userRole.setName("ROLE_USER");
		userRole.setPermissions(userPermissions);
		userTypeRepository.save(userRole);

		UserType adminRole = new UserType();
		adminRole.setName("ROLE_ADMIN");
		adminRole.setPermissions(adminPermissions);
		userTypeRepository.save(adminRole);

		UserType companyOwnerRole = new UserType();
		companyOwnerRole.setName("ROLE_COMPANY_OWNER");
		companyOwnerRole.setPermissions(companyOwnerPermissions);
		userTypeRepository.save(companyOwnerRole);

		User admin = new User(1L, "sanja.drinic776@gmail.com", "sanja", passwordEncoder.encode("sanja"), Timestamp.from(Instant.now()), "Sanja","Drinic", true, adminRole, null);
		userRepository.save(admin);
	}

	private Set<Permission> getCompanyOwnerPermissions() {
		Set<Permission> companyOwnerPermissions = new HashSet<>();
		Permission changePassword = new Permission("changePassword");
		companyOwnerPermissions.add(changePassword);

		Permission getAllCompanyCommentsByCompanyId = new Permission("getAllCompanyCommentsByCompanyId");
		companyOwnerPermissions.add(getAllCompanyCommentsByCompanyId);

		Permission getAllSalaryCommentsByCompanyId = new Permission("getAllSalaryCommentsByCompanyId");
		companyOwnerPermissions.add(getAllSalaryCommentsByCompanyId);

		Permission getAllInterviewCommentsByCompanyId = new Permission("getAllInterviewCommentsByCompanyId");
		companyOwnerPermissions.add(getAllInterviewCommentsByCompanyId);

		Permission saveJobOffer = new Permission("saveJobOffer");
		companyOwnerPermissions.add(saveJobOffer);

		Permission getAllJobOffers = new Permission("getAllJobOffers");
		companyOwnerPermissions.add(getAllJobOffers);

		Permission updateCompanyInfo = new Permission("updateCompanyInfo");
		companyOwnerPermissions.add(updateCompanyInfo);

		Permission getAllCompanies = new Permission("getAllCompanies");
		companyOwnerPermissions.add(getAllCompanies);

		Permission getAllUsers = new Permission("getAllUsers");
		companyOwnerPermissions.add(getAllUsers);

		Permission getUserByUsername = new Permission("getUserByUsername");
		companyOwnerPermissions.add(getUserByUsername);

		Permission getUserById = new Permission("getUserById");
		companyOwnerPermissions.add(getUserById);

		Permission getCompanyByOwnerUsername = new Permission("getCompanyByOwnerUsername");
		companyOwnerPermissions.add(getCompanyByOwnerUsername);

		return companyOwnerPermissions;
	}

	private Set<Permission> getUserPermissions() {
		Set<Permission> userPermissions = new HashSet<>();
		Permission changePassword = new Permission("changePassword");
		userPermissions.add(changePassword);

		// comment controller
		Permission addCompanyComment = new Permission("addCompanyComment");
		userPermissions.add(addCompanyComment);

		Permission getAllCompanyCommentsByCompanyId = new Permission("getAllCompanyCommentsByCompanyId");
		userPermissions.add(getAllCompanyCommentsByCompanyId);

		Permission getPositionsByCompanyId = new Permission("getPositionsByCompanyId");
		userPermissions.add(getPositionsByCompanyId);

		Permission addSalaryComment = new Permission("addSalaryComment");
		userPermissions.add(addSalaryComment);

		Permission getAllSalaryCommentsByCompanyId = new Permission("getAllSalaryCommentsByCompanyId");
		userPermissions.add(getAllSalaryCommentsByCompanyId);

		Permission addInterviewComment = new Permission("addInterviewComment");
		userPermissions.add(addInterviewComment);

		Permission getAllInterviewCommentsByCompanyId = new Permission("getAllInterviewCommentsByCompanyId");
		userPermissions.add(getAllInterviewCommentsByCompanyId);

		// company controller
		Permission saveRegistrationRequest = new Permission("saveRegistrationRequest");
		userPermissions.add(saveRegistrationRequest);

		Permission getAllJobOffers = new Permission("getAllJobOffers");
		userPermissions.add(getAllJobOffers);

		Permission getAllCompanies = new Permission("getAllCompanies");
		userPermissions.add(getAllCompanies);

		// user controller
		Permission getAllUsers = new Permission("getAllUsers");
		userPermissions.add(getAllUsers);

		Permission getUserByUsername = new Permission("getUserByUsername");
		userPermissions.add(getUserByUsername);

		Permission getUserById = new Permission("getUserById");
		userPermissions.add(getUserById);

		return userPermissions;
	}

	private Set<Permission> getAdminPermissions() {
		Set<Permission> adminPermissions = new HashSet<>();
		Permission changePassword = new Permission("changePassword");
		adminPermissions.add(changePassword);

		Permission getAllCompanyCommentsByCompanyId = new Permission("getAllCompanyCommentsByCompanyId");
		adminPermissions.add(getAllCompanyCommentsByCompanyId);

		Permission getAllSalaryCommentsByCompanyId = new Permission("getAllSalaryCommentsByCompanyId");
		adminPermissions.add(getAllSalaryCommentsByCompanyId);

		Permission getAllInterviewCommentsByCompanyId = new Permission("getAllInterviewCommentsByCompanyId");
		adminPermissions.add(getAllInterviewCommentsByCompanyId);

		Permission approveRequest = new Permission("approveRequest");
		adminPermissions.add(approveRequest);

		Permission rejectRequest = new Permission("rejectRequest");
		adminPermissions.add(rejectRequest);

		Permission getAllJobOffers = new Permission("getAllJobOffers");
		adminPermissions.add(getAllJobOffers);

		Permission getAllRequests = new Permission("getAllRequests");
		adminPermissions.add(getAllRequests);

		Permission getAllCompanies = new Permission("getAllCompanies");
		adminPermissions.add(getAllCompanies);

		Permission getAllUsers = new Permission("getAllUsers");
		adminPermissions.add(getAllUsers);

		Permission getUserByUsername = new Permission("getUserByUsername");
		adminPermissions.add(getUserByUsername);

		Permission getUserById = new Permission("getUserById");
		adminPermissions.add(getUserById);

		//?
		Permission saveJobOffer = new Permission("saveJobOffer");
		adminPermissions.add(saveJobOffer);

		return adminPermissions;
	}

}
