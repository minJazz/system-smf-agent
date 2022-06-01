package kr.co.smf.agent.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import kr.co.smf.agent.beans.Agent;
import kr.co.smf.agent.util.AgentUtil;

public class UserServlet extends HttpServlet {
	private AgentUtil agentUtil;
	
	public UserServlet() {
		agentUtil = new AgentUtil();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader bufferedReader = null;
		String responseJson = "";
		try {
			bufferedReader = request.getReader();

			StringBuffer resultBuffer = new StringBuffer();
			String line = null;
			line = bufferedReader.readLine();
			
			JSONObject jsonObject = new JSONObject(line);
			
			String userMail = jsonObject.getString("userMail");
			
			Agent agent = new Agent();
			agent.setUserMail(userMail);
			
			agentUtil.updateAgentInfoFile(agent);
			
			responseJson = "{" +"'code':200"+"}";
					
		} catch (IOException e) {
			responseJson = "{" +"'code':300, 'message':'"+ e.getMessage() + "'}";
		} finally {
			try {
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				
				out.print(responseJson);
				out.flush();
				
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
