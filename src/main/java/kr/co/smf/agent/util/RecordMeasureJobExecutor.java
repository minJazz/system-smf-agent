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
			
			StringBuffer json = new StringBuffer();
			json.append("{");
			json.append("  \"agentIpAddress\":\"" + measurement.getAgentIpAddress() + "\"");
			json.append("  \"co2\":\"" + measurement.getCo2() + "\"");
			json.append("  \"humidity\":\"" + measurement.getHumidity() + "\"");
			json.append("  \"Temperature\":\"" + measurement.getTemperature() + "\"");
			json.append("}");
			
			System.out.println("----> test " + json);
			File photo = new File("/home/mybatis/sensor/photo.png");
			
			RequestBody requestBody = new MultipartBody
					.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("photo", photo.getName(), RequestBody.create(MULTIPART, photo))
					.addFormDataPart("measurement", json.toString())
					.build();
			
			Request request = new Request.Builder().url("/127.0.0.1:8080/record-info").post(requestBody).build();
			Response response = client.newCall(request).execute();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
