package com.gtel.api.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerJobInfo {
    private String jobId;
    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String cronExpression;
    private Boolean cronJob;
    private Long repeatTime;
    private String jobClass;
    private String description;
}
