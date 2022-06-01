package kr.co.smf.agent.util;

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
		return null;
	}
	
	public boolean updateGrowthSettingFile(Setting setting) {
		return false;
	}
	
	public Agent selectAgentInfoFile() {
		return null;
	}
	
	public boolean updateAgentInfoFile(Agent agent) {
		return false;
	}
	
	public Measurement selectGrowthMeasurementFile() {
		return null;
	}
	
	public boolean updateGrowthMeasurementFile(Measurement measurement) {
		return false;
	}
}
