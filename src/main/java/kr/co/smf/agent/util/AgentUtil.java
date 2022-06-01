package kr.co.smf.agent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;

import kr.co.smf.agent.beans.Agent;
import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;

public class AgentUtil {
	private String agentPropertiesPath;
	private String measurementPropertiesPath;
	private String settingPropertiesPath;

	public void setAgentPropertiesPath(String agentPropertiesPath) {
		this.agentPropertiesPath = agentPropertiesPath;
	}
	
	public void setMeasurementPropertiesPath(String measurementPropertiesPath) {
		this.measurementPropertiesPath = measurementPropertiesPath;
	}


	public void setSettingPropertiesPath(String settingPropertiesPath) {
		this.settingPropertiesPath = settingPropertiesPath;
	}

	public void sendAgentInfo(Agent agent) {

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

			properties.setProperty("settingName", setting.getSettingName());
			properties.setProperty("userPhoneNumber", setting.getUserPhoneNumber());
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
		return null;
	}

	public boolean updateAgentInfoFile(Agent agent) {
		return false;
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
			measurement.setTemperature(Double.valueOf(properties.getProperty("temperature")));
			measurement.setHumidity(Integer.valueOf(properties.getProperty("humidity")));
			measurement.setCo2(Integer.valueOf(properties.getProperty("co2")));

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
			fileReader = new FileReader
				("." + File.separator + "WebContent"  
			         + File.separator + "WEB-INF"  
			         + File.separator + "measurement.properties");

			Properties properties = new Properties();
			properties.load(fileReader);
			
			properties.setProperty("agentIpAddress", measurement.getAgentIpAddress());
			properties.setProperty("temperature", String.valueOf(measurement.getTemperature()));
			properties.setProperty("humidity", String.valueOf(measurement.getHumidity()));
			properties.setProperty("co2", String.valueOf(measurement.getCo2()));
			
			fileOutputStream = new FileOutputStream
					("." + File.separator + "WebContent"  
			             + File.separator + "WEB-INF"  
			             + File.separator + "measurement.properties");
			
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
