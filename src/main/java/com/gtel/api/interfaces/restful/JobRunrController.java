package com.gtel.api.interfaces.restful;

import com.gtel.api.application.service.JobRunrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/job-runr")
@RequiredArgsConstructor
public class JobRunrController {
    private final JobRunrService jobRunrService;

    @PostMapping("create/{id}")
    public String createJob(@PathVariable String id) {
        return this.jobRunrService.testJob(id);
    }

    @PostMapping("clear/{id}")
    public String clearJob(@PathVariable String id) {
        return this.jobRunrService.clearJob(id);
    }
}