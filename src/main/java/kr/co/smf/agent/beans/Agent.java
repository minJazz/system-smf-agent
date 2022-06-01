package kr.co.smf.agent.beans;

import java.io.Serializable;

public class Agent implements Serializable {
	private String previousAgentIpAddress;
	private String nowAgentIpAddress;
	private String userMail;

	public Agent() {
	}

	public String getPreviousAgentIpAddress() {
		return previousAgentIpAddress;
	}

	public void setPreviousAgentIpAddress(String previousAgentIpAddress) {
		this.previousAgentIpAddress = previousAgentIpAddress;
	}

	public String getNowAgentIpAddress() {
		return nowAgentIpAddress;
	}

	public void setNowAgentIpAddress(String nowAgentIpAddress) {
		this.nowAgentIpAddress = nowAgentIpAddress;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
}
