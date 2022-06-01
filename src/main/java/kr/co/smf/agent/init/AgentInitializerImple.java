package kr.co.smf.agent.init;

import java.io.IOException;

import javax.servlet.ServletContextEvent;

import kr.co.smf.agent.beans.Agent;
import kr.co.smf.agent.util.AgentUtil;

public class AgentInitializerImple implements AgentInitializer {
	private AgentUtil agentUtil;

	public AgentInitializerImple() {
		agentUtil = new AgentUtil();
	}

	@Override
	public boolean requestAddAgent() {
		Agent agent = agentUtil.selectAgentInfoFile();

		if (agent.getPreviousAgentIpAddress() == null || "".equals(agent.getPreviousAgentIpAddress())) {
			agent.setPreviousAgentIpAddress("empty");
		}

		// TODO 현재 ip 등록

		if (!agent.getPreviousAgentIpAddress().equals(agent.getNowAgentIpAddress())) {
			try {
				agentUtil.sendAgentInfo(agent);
			} catch (Exception e) {
				e.printStackTrace();
				
				return false;
			}
		}

		return agentUtil.updateAgentInfoFile(agent);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		int count = 0;
		while (true) {
			if (requestAddAgent() || count++ > 9) {
				break;
			}
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
