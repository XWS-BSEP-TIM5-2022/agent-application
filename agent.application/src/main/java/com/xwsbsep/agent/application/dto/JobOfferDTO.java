package com.xwsbsep.agent.application.dto;

import com.xwsbsep.agent.application.model.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferDTO {
    private Long id;
    private Long companyId;
    private Position position;
    private String jobDescription;
    private String dailyActivities;
    private String preconditions;
}
