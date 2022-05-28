package com.xwsbsep.agent.application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "positions")
@Getter
@Setter
public class Position {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long pay;
}
