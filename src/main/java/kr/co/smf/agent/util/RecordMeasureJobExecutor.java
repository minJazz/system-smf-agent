package kr.co.smf.agent.util;

import java.io.File;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import kr.co.smf.agent.beans.Measurement;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordMeasureJobExecutor implements Job {
	private OkHttpClient client;
	private AgentUtil agentUtil;
	private final MediaType MULTIPART = MediaType.parse("multipart/form-data");
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			agentUtil = new AgentUtil();
			client = new OkHttpClient();
			
			Measurement measurement = agentUtil.selectGrowthMeasurementFile();
			
			StringBuffer data = new StringBuffer();
			data.append(measurement.getAgentIpAddress()  + ":");
			data.append(measurement.getCo2() + ":");
			data.append(measurement.getHumidity() + ":");
			data.append(measurement.getTemperature());

			
			File photo = new File("/home/mybatis/sensor/photo.png");
			
			RequestBody requestBody = new MultipartBody.Builder()
														.setType(MultipartBody.FORM)
														.addFormDataPart("photo", photo.getName(), RequestBody.create(MULTIPART, photo))
														.addFormDataPart("measurement", data.toString())
														.build();
			
			Request request = new Request.Builder().url("http://" + agentUtil.getServerIp() + "/record-info").post(requestBody).build();
			Response response = client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
