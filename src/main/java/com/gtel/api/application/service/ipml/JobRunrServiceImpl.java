package com.gtel.api.application.service.ipml;

import com.gtel.api.application.service.JobRunrService;
import com.gtel.api.domains.models.postgres.Test;
import com.gtel.api.infrastracture.repository.postgres.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.StorageProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@Service
@Log4j2
@RequiredArgsConstructor
public class JobRunrServiceImpl implements JobRunrService {

    private final TestRepository testRepository;

    private final JobScheduler jobScheduler;

    private final StorageProvider storageProvider;


    @Override
    public String testJob(String id) {
//        Optional<Test> test = testRepository.findById(id);
//        if (test.isPresent()) {
//            Test test1 = test.get();
//            test1.setIsRequestDelete("1");
//            testRepository.save(test1);
//            String jobName = "JOB_USER_" + id;
//            UUID jobId = jobScheduler.schedule(
//                    Instant.now().plus(30, ChronoUnit.SECONDS),
//                    () -> deleteUserAfterDelay(id)
//            ).asUUID();
//
//
//            log.info("✅ Job {} scheduled for user {}", jobId, id);
//
//            return "Đã lên lịch vô hiệu hóa tài khoản " + id + " sau 30s.";
//
//        }
        Instant executionTime = Instant.now().plus(Duration.ofSeconds(10));

        String jobName = "JOB_USER_" + id;
        JobId jobId = jobScheduler.create(aJob()
                .withName(jobName)
                .withDetails(() -> printDele(id))
                .scheduleAt(executionTime));
        log.info("✅ã tạo job {} cho user {}", jobId, id);

        return "success";
    }

    public void printDele(String id) {
        log.info("Delete account " + id);
    }

    public void deleteUserAfterDelay(String id) {
        Optional<Test> test = testRepository.findById(id);
        if (test.isPresent()) {
            Test test1 = test.get();
            test1.setIsDelete("1");
            testRepository.save(test1);
            log.info("User {} has been delete", id);
        }
    }



    @Override
    public String clearJob(String userId) {
        return "";
    }
}
