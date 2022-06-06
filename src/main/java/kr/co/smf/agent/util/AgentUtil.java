package kr.co.smf.agent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.smf.agent.beans.Agent;
import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AgentUtil {
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private String agentPropertiesPath = "/home/mybatis/Desktop/scheduler/agent.properties";
	private String measurementPropertiesPath = "/home/mybatis/Desktop/scheduler/measurement.properties";
	private String settingPropertiesPath = "/home/mybatis/Desktop/scheduler/setting.properties";
	private String serverIp;

	private OkHttpClient client;
	private ObjectMapper objectMapper;

	public AgentUtil() {
		client = new OkHttpClient();
		objectMapper = new ObjectMapper();
	}


	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerIp() {
		Setting setting = new Setting();
		FileReader fileReader = null;
		Properties properties = new Properties();
		String serverIp = null;

		try {
			fileReader = new FileReader(new File(agentPropertiesPath));
			properties.load(fileReader);
			serverIp = properties.getProperty("serverIp");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return serverIp;
	}

	public void sendAgentInfo(Agent agent) throws IOException {
		String json = "{\"previousAgentIpAddress\" : \"" + agent.getPreviousAgentIpAddress() + "\", "
				+ "\"nowAgentIpAddress\" : \"" + agent.getNowAgentIpAddress() + "\", " + "\"userMail\" : \""
				+ agent.getUserMail() + "\"}";
		System.out.println("json " + json);

		RequestBody body = RequestBody.create(JSON, json);

		Request request = new Request.Builder().url("http://" + serverIp + "/agent-info").put(body).build();

		Response response = client.newCall(request).execute();

		ResponseBody responseBody = response.body();

		JSONObject jsonResponse = new JSONObject(responseBody.string());
    
		if (!jsonResponse.getString("code").equals("200")) {
			System.out.println("제어 요청 오류 : " + jsonResponse.getString("message")); // TODO Logger 추가 시 변경 요망
		}
	}

	public void sendGrowthRecordInfo(Measurement measurement) {

	}

	public Setting selectGrowthSettingFile() {
		Setting setting = new Setting();
		FileReader fileReader = null;
		Properties properties = new Properties();

		try {
			fileReader = new FileReader(new File(settingPropertiesPath));
			properties.load(fileReader);

			setting.setSettingName(properties.getProperty("settingName"));
			setting.setUserPhoneNumber(properties.getProperty("userPhoneNumber"));
			setting.setTemperature(Double.parseDouble(properties.getProperty("temperature")));
			setting.setHumidity(Integer.parseInt(properties.getProperty("humidity")));
			setting.setCo2(Integer.parseInt(properties.getProperty("co2")));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return setting;
	}

	public boolean updateGrowthSettingFile(Setting setting) {
		FileOutputStream fileOutputStream = null;
		FileReader fileReader = null;
		Properties properties = new Properties();

		try {
			fileReader = new FileReader(new File(settingPropertiesPath));
			properties.load(fileReader);

			properties.setProperty("temperature", String.valueOf(setting.getTemperature()));
			properties.setProperty("humidity", String.valueOf(setting.getHumidity()));
			properties.setProperty("co2", String.valueOf(setting.getCo2()));

			fileOutputStream = new FileOutputStream(settingPropertiesPath);
			properties.store(fileOutputStream, "갱신");
			fileOutputStream.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return true;
	}

	public Agent selectAgentInfoFile() {
		Agent agent = new Agent();
		FileReader fileReader = null;
		Properties properties = new Properties();

		try {
			fileReader = new FileReader(new File(agentPropertiesPath));
			properties.load(fileReader);

			agent.setPreviousAgentIpAddress(properties.getProperty("ipAddress"));
			agent.setUserMail(properties.getProperty("userMail"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return agent;
	}

	public boolean updateAgentInfoFile(Agent agent) {
		FileOutputStream fileOutputStream = null;
		FileReader fileReader = null;
		Properties properties = new Properties();

		try {
			fileReader = new FileReader(new File(agentPropertiesPath));
			properties.load(fileReader);

			properties.setProperty("ipAddress", agent.getNowAgentIpAddress());
			properties.setProperty("userMail", agent.getUserMail());

			fileOutputStream = new FileOutputStream(agentPropertiesPath);
			properties.store(fileOutputStream, "갱신");
			fileOutputStream.flush();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return true;
	}

	public Measurement selectGrowthMeasurementFile() {
		FileReader fileReader = null;
		Measurement measurement = null;
		
		try {
			fileReader = new FileReader(measurementPropertiesPath);

			Properties properties = new Properties();
			properties.load(fileReader);
			
			measurement = new Measurement();
			measurement.setAgentIpAddress(properties.getProperty("agentIpAddress"));
			measurement.setCo2(Integer.valueOf(properties.getProperty("co2")));
			measurement.setTemperature(Double.valueOf(properties.getProperty("temperature")));
			measurement.setHumidity(Integer.valueOf(properties.getProperty("humidity")));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return measurement;
	}

	public boolean updateGrowthMeasurementFile(Measurement measurement) {
		FileReader fileReader = null;
		FileOutputStream fileOutputStream = null;

		try {
			fileReader = new FileReader(measurementPropertiesPath);

			Properties properties = new Properties();
			properties.load(fileReader);

			properties.setProperty("temperature", String.valueOf(measurement.getTemperature()));
			properties.setProperty("humidity", String.valueOf(measurement.getHumidity()));
			properties.setProperty("co2", String.valueOf(measurement.getCo2()));

			fileOutputStream = new FileOutputStream(measurementPropertiesPath);

			properties.store(fileOutputStream, "갱신");
			fileOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
