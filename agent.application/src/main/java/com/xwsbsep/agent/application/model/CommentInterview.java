package com.xwsbsep.agent.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "interview_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentInterview {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name="position", nullable = false)
    @NotBlank(message = "CommentInterview position is required")
    private String position;

    @Column(name="title", nullable = false)
    @NotBlank(message = "CommentInterview title is required")
    private String title;

    @Column(name="hr_interview", nullable = false, columnDefinition = "text")
    @NotBlank(message = " CommentInterview HR interview is required")
    private String hrInterview;

    @Column(name="technical_interview", nullable = false, columnDefinition = "text")
    @NotBlank(message = "CommentInterview technical interview is required")
    private String technicalInterview;

    @Column(name="rating", nullable = false)
    @NotNull(message = "CommentInterview rating is required")
    private Integer rating;
}
