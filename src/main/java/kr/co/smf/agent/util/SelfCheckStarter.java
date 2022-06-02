package kr.co.smf.agent.util;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SelfCheckStarter {

	public static void main(String[] args) {
		try {

			// Scheduler 생성
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();

			// JOB Executor Class
			Class<? extends Job> jobClass = SelfCheckJob.class;

			// JOB 생성
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity("job_name", "job_group").build();

			// CronTrigger 생성 CronTrigger는 주기적으로 반복
			// 5초주기로 반복
			CronScheduleBuilder cronSch = CronScheduleBuilder.cronSchedule(new CronExpression("0/10 * * * * ?"));
			CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withIdentity("cron_trigger", "cron_trigger_group").withSchedule(cronSch).forJob(jobDetail).build();

			// Schedule 등록
			// scheduler.scheduleJob(JobDetail,Trigger,true or false) -> 다수 트리거 추가시 사용
			// scheduler.scheduleJob(JobDetail, Trigger) -> 하나의 트리거 사용
			scheduler.scheduleJob(jobDetail, cronTrigger);
			// Scheduler 실행
			scheduler.start();

		} catch (ParseException | SchedulerException e) {
			e.printStackTrace();
		}
	}
}
