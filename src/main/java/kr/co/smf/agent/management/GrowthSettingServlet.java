package kr.co.smf.agent.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;

public class GrowthSettingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		GrowthManagementServiceImple gms = new GrowthManagementServiceImple();
		ServletContext servletContext = this.getServletContext();
		String path = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "setting.properties");

		Setting setting = gms.viewSettingValue(path);

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
		GrowthManagementServiceImple gms = new GrowthManagementServiceImple();
		ServletContext servletContext = this.getServletContext();
		String path = servletContext.getRealPath(File.separator + "WEB-INF" + File.separator + "setting.properties");

		BufferedReader bufferedReader = null;
		String responseJson = "";
		try {
			bufferedReader = request.getReader();

			StringBuilder line = new StringBuilder();
			String buffer = null;

			while ((buffer = bufferedReader.readLine()) != null) {
				line.append(buffer);
			}

			JSONObject jsonObject = new JSONObject(line.toString());

			Setting setting = new Setting();
			setting.setHumidity(jsonObject.getInt("humidity"));
			setting.setCo2(jsonObject.getInt("co2"));
			setting.setTemperature(jsonObject.getInt("temperature"));

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			if (gms.editSettingValue(setting, path)) {
				responseJson = "{\"code\":\"200\", \"msg\":\"ok\"}";
			} else {
				responseJson = "{\"code\":\"300\", \"msg\":\"error\"}";
			}

			PrintWriter out = response.getWriter();
			out.print(responseJson);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
