package com.gtel.api.application.service.ipml;

import com.gtel.api.application.service.JobService;
import com.gtel.api.application.service.dto.SchedulerJobInfo;
import com.gtel.api.configs.JobScheduleCreator;
import com.gtel.api.domains.models.postgres.Test;
import com.gtel.api.infrastracture.repository.postgres.TestRepository;
import com.gtel.api.jobs.DeleteAccountJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final TestRepository testRepository;

    private final Scheduler scheduler;

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final JobScheduleCreator scheduleCreator;

    @Override
    public String testJob(String id) {
        Optional<Test> test = testRepository.findById(id);
        if (test.isPresent()) {
            Test test1 = test.get();
            test1.setIsRequestDelete("1");
            testRepository.save(test1);
            this.scheduleNewMulticastJob(id, new Date(System.currentTimeMillis() + 1 * 60 * 1000));
            log.info("📌 Đã lên lịch vô hiệu hóa tài khoản " + id + " sau 1p.");

            return "📌 Đã lên lịch vô hiệu hóa tài khoản " + id + " sau 1p.";

        }
        return "";
    }

    private void scheduleNewMulticastJob(String userId, Date startTime) {
        try {
            SchedulerJobInfo jobInfo = new SchedulerJobInfo();
            jobInfo.setJobName("JobDeleteAccount-" + userId);
            jobInfo.setJobClass(DeleteAccountJob.class.getName());
            jobInfo.setRepeatTime(0L);
            jobInfo.setDescription("Job delete account Description");

            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            Map<String, String> jobDataMap = new HashMap<>();
            jobDataMap.put("userId", userId);
            JobDetail jobDetail = scheduleCreator.createJobDetail(jobInfo, jobDataMap);

            Trigger trigger = scheduleCreator.createTrigger(jobInfo, startTime, 1, jobDataMap);

            scheduler.scheduleJob(jobDetail, trigger);
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");

        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String clearJob(String userId) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            String jobName = "JobDeleteAccount-" + userId; // Tạo tên job theo userId
            JobKey jobKey = new JobKey(jobName, "DEFAULT"); // Group mặc định

            if (scheduler.checkExists(jobKey)) {
                boolean deleted = scheduler.deleteJob(jobKey);
                if (deleted) {
                    log.info("Job for userId " + userId + " has been cancelled.");
                    return "Job for userId " + userId + " has been cancelled.";
                } else {
                    return "Failed to cancel job for userId " + userId;
                }
            } else {
                return "No job found for userId " + userId;
            }
        } catch (SchedulerException e) {
            return "Error while cancelling job: " + e.getMessage();
        }
    }
}
