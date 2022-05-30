package com.xwsbsep.agent.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Name of company is mandatory!")
    private String name;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description of company is mandatory!")
    private String description;

    @Column(name = "phone_number", nullable = false)
    @NotBlank(message = "Phone number of company is mandatory!")
    private String phoneNumber;

    @OneToMany(mappedBy="company") //fetch = FetchType.EAGER
    private Set<JobOffer> jobOffers;

    private boolean isActive;

    //    @OneToOne(mappedBy="company")
    //    private User owner;

    // TODO: komentari, plate, ocene, proces selekcije kandidata
}
