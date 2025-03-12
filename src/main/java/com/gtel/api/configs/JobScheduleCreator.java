package com.gtel.api.configs;

import com.gtel.api.application.service.dto.SchedulerJobInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;


@Component
public class JobScheduleCreator {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final ApplicationContext applicationContext;
    private final SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobScheduleCreator(ApplicationContext applicationContext, SchedulerFactoryBean schedulerFactoryBean) {
        this.applicationContext = applicationContext;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    public Scheduler getScheduler() {
        return this.schedulerFactoryBean.getScheduler();
    }

    public Trigger createTrigger(SchedulerJobInfo jobInfo, Date startTime, int misFireInstruction,
                                 Map<String, String> map) {
        if (Boolean.TRUE.equals(jobInfo.getCronJob())) {
            return this.createCronTrigger(jobInfo, startTime, misFireInstruction, map);
        } else {
            return this.createSimpleTrigger(jobInfo, startTime, misFireInstruction, map);
        }
    }

    private CronTrigger createCronTrigger(SchedulerJobInfo jobInfo, Date startTime, int misFireInstruction,
                                          Map<String, String> map) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(jobInfo.getJobName());
        factoryBean.setGroup(jobInfo.getJobGroup());
        factoryBean.setStartTime(startTime);
        factoryBean.setCronExpression(jobInfo.getCronExpression());
        factoryBean.setMisfireInstruction(misFireInstruction);

        if ((map != null) && !map.isEmpty()) {
            factoryBean.setJobDataMap(new JobDataMap(map));
        }
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            this.log.error(e.getMessage(), e);
        }
        return factoryBean.getObject();
    }

    private SimpleTrigger createSimpleTrigger(SchedulerJobInfo jobInfo, Date startTime, int misFireInstruction,
                                              Map<String, String> map) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(jobInfo.getJobName());
        factoryBean.setGroup(jobInfo.getJobGroup());
        factoryBean.setStartTime(startTime);
        factoryBean.setRepeatInterval(jobInfo.getRepeatTime());
        factoryBean.setRepeatCount(SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY);
        factoryBean.setMisfireInstruction(misFireInstruction);
        if ((map != null) && !map.isEmpty()) {
            factoryBean.setJobDataMap(new JobDataMap(map));
        }
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    private JobDetail createJob(Class<? extends Job> jobClass, String jobName, String jobGroup,
                                Map<String, String> map) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(this.applicationContext);
        factoryBean.setName(jobName);
        factoryBean.setGroup(jobGroup);

        if ((map != null) && !map.isEmpty()) {
            factoryBean.setJobDataMap(new JobDataMap(map));
        }
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    public JobDetail createJobDetail(SchedulerJobInfo jobInfo, Map<String, String> map) {
        try {
            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            Class<?> clazz = Class.forName(jobInfo.getJobClass());
            Class<? extends Job> jobClass = clazz.asSubclass(Job.class);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
                    .build();
            if (scheduler.checkExists(jobDetail.getKey())) {
                if (scheduler.deleteJob(jobDetail.getKey())) {
                    return this.createJob(jobClass, jobInfo.getJobName(), jobInfo.getJobGroup(), map);
                } else {
                    this.log.warn("scheduleNewJobRequest.jobAlreadyExist [{}-{}]", jobInfo.getJobGroup(),
                            jobInfo.getJobName());
                }
            } else {
                return this.createJob(jobClass, jobInfo.getJobName(), jobInfo.getJobGroup(), map);
            }
        } catch (ClassNotFoundException | SchedulerException e) {
            this.log.error(e.getMessage(), e);
        }
        return null;
    }
}
