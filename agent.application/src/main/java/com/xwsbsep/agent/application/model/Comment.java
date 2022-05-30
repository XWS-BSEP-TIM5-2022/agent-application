package com.xwsbsep.agent.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name="title", nullable = false)
    @NotBlank(message = "Comment title is required")
    private String title;

    @Column(name="content", nullable = false, columnDefinition = "text")
    @NotBlank(message = "Comment content is required")
    private String content;

    @Column(name="rating", nullable = false)
    @NotNull(message = "Comment rating is required")
    private Integer rating;

    @Column(name="position", nullable = false)
    @NotBlank(message = "Comment position is required")
    private String position;
}
