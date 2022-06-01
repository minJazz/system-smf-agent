package kr.co.smf.agent.management;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.smf.agent.beans.Setting;
import kr.co.smf.agent.util.AgentUtil;

public class GrowthSettingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = this.getServletContext();
		String path = servletContext.getRealPath("/"+File.separator+"WEB-INF"+File.separator+"setting.properties");

		AgentUtil agentUtil = new AgentUtil();
		agentUtil.setSettingPropertiesPath(path);
		Setting setting = agentUtil.selectGrowthSettingFile();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(setting);
		out.print(json);
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = this.getServletContext();
		String path = servletContext.getRealPath("/WEB-INF/setting.properties");

		AgentUtil agentUtil = new AgentUtil();
		agentUtil.setSettingPropertiesPath(path);
		
		
		//boolean test = agentUtil.updateGrowthSettingFile(setting);
	}
}
