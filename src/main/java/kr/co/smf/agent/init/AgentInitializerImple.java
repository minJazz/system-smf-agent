package kr.co.smf.agent.init;

import javax.servlet.ServletContextEvent;

public class AgentInitializerImple implements AgentInitializer {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public boolean requestAddAgent() {
		return false;
	}

}
