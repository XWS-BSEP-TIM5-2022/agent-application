package com.xwsbsep.agent.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveRequestDTO {
    private Long id;    // id request-a
    private boolean approved;
}
