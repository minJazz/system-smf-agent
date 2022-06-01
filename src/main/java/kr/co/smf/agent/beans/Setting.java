package kr.co.smf.agent.beans;

import java.io.Serializable;

public class Setting implements Serializable {
	private String userPhoneNumber;
	private String settingName;
	private double temperature;
	private int humidity;
	private int co2;
	private String settingTime;

	public Setting() {
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getCo2() {
		return co2;
	}

	public void setCo2(int co2) {
		this.co2 = co2;
	}

	public String getSettingTime() {
		return settingTime;
	}

	public void setSettingTime(String settingTime) {
		this.settingTime = settingTime;
	}
}
