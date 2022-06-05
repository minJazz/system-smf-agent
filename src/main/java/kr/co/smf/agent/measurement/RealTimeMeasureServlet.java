package kr.co.smf.agent.measurement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.util.AgentUtil;

public class RealTimeMeasureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	
    	AgentUtil agentUtil = new AgentUtil();
    	
    	Measurement measurement = agentUtil.selectGrowthMeasurementFile();
    	
    	
    	response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(measurement);
		out.print(json);
		out.flush();
    }
}
