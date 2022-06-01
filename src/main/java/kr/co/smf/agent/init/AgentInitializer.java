package kr.co.smf.agent.init;

import javax.servlet.ServletContextListener;

public interface AgentInitializer extends ServletContextListener {
	public boolean requestAddAgent();
}
