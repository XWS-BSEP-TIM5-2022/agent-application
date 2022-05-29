package com.xwsbsep.agent.application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "positions")
@Getter
@Setter
public class Position {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name of position is mandatory!")
    private String name;

    @Min(value = 100, message = "Pay can not be less than 100!")
    @Positive(message = "Pay is a positive value!")     // TODO:
    private Long pay;
}
