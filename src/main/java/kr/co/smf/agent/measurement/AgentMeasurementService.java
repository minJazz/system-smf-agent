package kr.co.smf.agent.measurement;

import java.io.File;
import java.util.Map;

import kr.co.smf.agent.beans.Measurement;

public interface AgentMeasurementService {
	public String recordMeasurement(Map<String, String> value);

	public Measurement measureGrowthInfo();

	public File shootPhoto();
}
