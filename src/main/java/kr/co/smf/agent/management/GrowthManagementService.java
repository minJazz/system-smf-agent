package kr.co.smf.agent.management;

import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;

public interface GrowthManagementService {
	public boolean manageGrowth();

	public void controlFan(boolean state);

	public void controlHeater(boolean state);

	public void controlHumidified(boolean state);

	public boolean notifyEmergency(Setting setting, Measurement measurement);

	public boolean changeDestination(String value, String path);

	public Setting viewSettingValue(String path);

	public boolean editSettingValue(Setting setting, String path);
}
