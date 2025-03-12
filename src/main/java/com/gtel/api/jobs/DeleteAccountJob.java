package com.gtel.api.jobs;

import com.gtel.api.domains.models.postgres.Test;
import com.gtel.api.infrastracture.repository.postgres.TestRepository;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Log4j2
public class DeleteAccountJob implements Job {

    @Autowired
    TestRepository testRepository;

    // Constructor mặc định
    public DeleteAccountJob() {
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Start call job ");
        String userId = context.getJobDetail().getJobDataMap().getString("userId"); // Lấy userId từ JobDataMap

        log.info("🔹 Kiểm tra tài khoản userId: " + userId + " lúc " + LocalDateTime.now());

        Optional<Test> test = testRepository.findById(userId);

        if (test.isPresent()) {
            Test test1 = test.get();
            test1.setIsDelete("1");
            testRepository.save(test1);
            log.info("⛔ Tài khoản " + userId + " đã bị vô hiệu hóa!");
            return;
        }
        log.info("⛔ Tài khoản " + userId + " khong tim thay!");

    }
}
