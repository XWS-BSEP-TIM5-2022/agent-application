package com.xwsbsep.agent.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private String position;

    @Column(name="title", nullable = false, unique = true)
    private String title;

    @Column(name="hr_interview", nullable = false)
    private String hrInterview;

    @Column(name="technical_interview", nullable = false)
    private String technicalInterview;

    @Column(name="rating", nullable = false)
    private Integer rating;
}
