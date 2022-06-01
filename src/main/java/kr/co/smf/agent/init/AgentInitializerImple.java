package kr.co.smf.agent.init;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
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

		try {
			InetAddress local = InetAddress.getLocalHost();

			System.out.println("My PC IP :" + local.getHostAddress());

			agent.setNowAgentIpAddress(local.getHostAddress());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

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
		System.out.println("server init");

		ServletContext servletContext = sce.getServletContext();
		String path = servletContext
				.getRealPath("/" + File.separator + "WEB-INF" + File.separator + "agent.properties");

		System.out.println("path : " + path);

		agentUtil.setAgentPropertiesPath(path);

		int count = 0;
		while (true) {
			if (requestAddAgent() || count++ > -1) {
				break;
			}
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
