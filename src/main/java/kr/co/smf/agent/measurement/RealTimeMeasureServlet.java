package kr.co.smf.agent.measurement;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.util.AgentUtil;

public class RealTimeMeasureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	ServletContext servletContext = this.getServletContext();
    	
    	String directoryPath = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator +  "measurement.properties");
    	
    	AgentUtil agentUtil = new AgentUtil();
    	agentUtil.setMeasurementPropertiesPath(directoryPath);
    	
    	Measurement measurement = agentUtil.selectGrowthMeasurementFile();
    	
    	request.setAttribute("measurement", measurement);
    }
}
