package com.xwsbsep.agent.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy="company") //fetch = FetchType.EAGER
    private Set<JobOffer> jobOffers;

    //@OneToOne(mappedBy = "company")
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    //private User user;

//    @OneToOne(mappedBy = "company")
//    private CompanyRegistrationRequest request;
}
