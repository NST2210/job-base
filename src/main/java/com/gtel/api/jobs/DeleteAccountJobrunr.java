package com.gtel.api.jobs;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DeleteAccountJobrunr {
    public void executeJob(String jobId) {
        log.info("🔥 Job {} is running!", jobId);
        // Thực hiện logic cần chạy sau 15 phút
    }
}
