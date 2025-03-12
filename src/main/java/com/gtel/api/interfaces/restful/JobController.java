package com.gtel.api.interfaces.restful;

import com.gtel.api.application.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping("create/{id}")
    public String createJob(@PathVariable String id) {
        return this.jobService.testJob(id);
    }

    @PostMapping("clear/{id}")
    public String clearJob(@PathVariable String id) {
        return this.jobService.clearJob(id);
    }
}