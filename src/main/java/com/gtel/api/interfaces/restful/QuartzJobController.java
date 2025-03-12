package com.gtel.api.interfaces.restful;

import com.gtel.api.application.service.QuartzJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/quartz-job")
@RequiredArgsConstructor
public class QuartzJobController {
    private final QuartzJobService quartzJobService;

    @PostMapping("create/{id}")
    public String createJob(@PathVariable String id) {
        return this.quartzJobService.testJob(id);
    }

    @PostMapping("clear/{id}")
    public String clearJob(@PathVariable String id) {
        return this.quartzJobService.clearJob(id);
    }
}