package kr.co.smf.agent.measurement;

import java.io.File;
import java.util.Map;

import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.util.AgentUtil;

public class AgentMeasurementServiceImple implements AgentMeasurementService {

	private AgentUtil agentUtil;
	
	public AgentMeasurementServiceImple() {
        agentUtil = new AgentUtil();
	}

	@Override
	public void recordMeasurement(Map<String, String> value) {
		Measurement measurement = new Measurement();
		measurement.setAgentIpAddress(value.get("agentIpAddress"));
		measurement.setTemperature(Double.valueOf(value.get("temperature")));
		measurement.setHumidity(Integer.valueOf(value.get("agentIpAddress")));
		measurement.setCo2(Integer.valueOf(value.get("agentIpAddress")));
		
		agentUtil.updateGrowthMeasurementFile(measurement);
	}

	@Override
	public Measurement measureGrowthInfo() {
		return agentUtil.selectGrowthMeasurementFile();
	}

	@Override
	public void shootPhoto() {
		String command = "sudo raspistill -o /home/mybatis/sensor/photo.png -w 640 -h 400";
		
		try {
			Process process = Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
