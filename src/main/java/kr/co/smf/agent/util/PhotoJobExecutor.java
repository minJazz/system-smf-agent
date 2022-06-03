package kr.co.smf.agent.util;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import kr.co.smf.agent.measurement.AgentMeasurementServiceImple;

public class PhotoJobExecutor implements Job {
	private AgentMeasurementServiceImple agentMeasurementService;
	
	
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
        agentMeasurementService = new AgentMeasurementServiceImple();
        agentMeasurementService.shootPhoto();
	}
}
