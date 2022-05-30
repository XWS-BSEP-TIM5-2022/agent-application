package com.xwsbsep.agent.application.model;

import com.xwsbsep.agent.application.dto.AddSalaryCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "salary_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSalary {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name="position", nullable = false)
    @NotBlank(message = "CommentSalary position is required")
    private String position;

    @Column(name="pay", nullable = false)
    @NotNull(message = "CommentSalary pay is required")
    private Integer pay;

    @Column(name="is_former_employee", nullable = false)
    @NotNull(message = "CommentSalary isFormerEmployee is required")
    private boolean isFormerEmployee;  // bivsi ili trenutni zaposleni

    @Column(name="bonus", nullable = false)
    @NotNull(message = "CommentSalary bonus is required")
    private boolean bonus;

    @Column(name="fair_pay", nullable = false)
    @NotNull(message = "CommentSalary fairPay is required")
    private boolean fairPay;

}
