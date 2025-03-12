package com.gtel.api.application.service;

import org.springframework.stereotype.Service;

@Service
public interface JobRunrService {
    String testJob(String id);
    String clearJob(String id);
}
