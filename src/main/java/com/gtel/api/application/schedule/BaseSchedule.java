//package com.gtel.api.application.schedule;
//
//import lombok.extern.log4j.Log4j2;
//import org.jobrunr.jobs.annotations.Job;
//import org.jobrunr.jobs.annotations.Recurring;
//import org.springframework.stereotype.Component;
//
//@Component
//@Log4j2
//public class BaseSchedule {
//
//    //job chay lien tuc sau moi tg cau hinh
//    @Recurring(id = "pay-bonus-ref", cron = "${schedule.pay-ref-bonus}")
//    @Job(name = "pay bonus ref")
//    public void exampleSchedule() {
//        log.info("pay bonus ref START");
//
//        log.info("pay bonus ref END");
//    }
//}
