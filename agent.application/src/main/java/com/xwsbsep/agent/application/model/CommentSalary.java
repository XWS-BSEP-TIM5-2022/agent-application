package com.xwsbsep.agent.application.model;

import com.xwsbsep.agent.application.dto.AddSalaryCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private String position;

    @Column(name="pay", nullable = false)
    private Integer pay;

    @Column(name="is_former_employee", nullable = false)
    private boolean isFormerEmployee;  // bivsi ili trenutni zaposleni

    @Column(name="bonus", nullable = false)
    private boolean bonus;

    @Column(name="fair_pay", nullable = false)
    private boolean fairPay;

}
