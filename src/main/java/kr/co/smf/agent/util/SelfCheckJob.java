package kr.co.smf.agent.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SelfCheckJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("5초 스케줄러 테스트");
		//TODO 생장환경 측정, 모듈 제어, 긴급알림 등 등 추가 
	}
}
