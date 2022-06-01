package kr.co.smf.agent.management;

import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;

public class GrowthManagementServiceImple implements GrowthManagementService {

	@Override
	public boolean manageGrowth() {
		return false;
	}

	@Override
	public void controlFan(boolean state) {
	}

	@Override
	public void controlHeater(boolean state) {
	}

	@Override
	public void controlHumidified(boolean state) {
	}

	@Override
	public boolean notifyEmergency(Setting setting, Measurement measurement) {
		return false;
	}

	@Override
	public boolean changeDestination(String value) {
		return false;
	}

	@Override
	public Setting viewSettingValue() {
		return null;
	}

	@Override
	public boolean editSettingValue(Setting setting) {
		return false;
	}

}
