package kr.co.smf.agent.management;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import kr.co.smf.agent.beans.Agent;
import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;
import kr.co.smf.agent.util.AgentUtil;

public class GrowthManagementServiceImple implements GrowthManagementService {
	AgentUtil agentUtil = new AgentUtil();

	@Override
	public boolean manageGrowth() {
		return false;
	}

	@Override
	public void controlFan(boolean state) {
	}

	@Override
	public void controlHeater(boolean state) {
	}

	@Override
	public void controlHumidified(boolean state) {
	}

	@Override
	public boolean notifyEmergency(Setting setting, Measurement measurement) {
//		setting.setCo2(2000);
//		setting.setHumidity(10);
//		setting.setTemperature(30);
//		
//		measurement.setCo2(1000);
//		measurement.setHumidity(30);
//		measurement.setTemperature(10);
		
		String recipient = "makewidely@gmail.com";
		// 1. 발신자의 메일 계정과 비밀번호 설정
		final String user = "alswo9588@gmail.com";
		final String password = "tsinezorohxtnhtw";
		// 2. Property에 SMTP 서버 정보 설정
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		// 3. SMTP 서버정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스 생성
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		// 4. Message 클래스의 객체를 사용하여 수신자와 내용, 제목의 메시지를 작성한다.
		// 5. Transport 클래스를 사용하여 작성한 메세지를 전달한다.
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			// 수신자메일주소
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("makewidely@gmail.com"));
			// Subject
			message.setSubject("생장환경 긴급 알림 메세지"); // 메일 제목을 입력
			// Text
			message.setText("농작물 생장환경에 문제가 발생하였습니다.\n" 
			+ "현재 농작물 생장환경 --> \t"+"[온도]\t" + measurement.getTemperature()+"℃\t"+ " [습도]\t" + measurement.getHumidity()+"%\t"+" [co2]\t" +measurement.getCo2()+"ppm"+"\n"
			+ "농작물 생장환경 설정 --> \t"+"[온도]\t" + setting.getTemperature()+"℃\t"+ " [습도]\t" + setting.getHumidity()+"%\t"+" [co2]\t" +setting.getCo2()+"ppm"); // 메일 내용을 입력
			// send the message
			Transport.send(message); //// 전송
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean changeDestination(String value, String path) {
		agentUtil.setAgentPropertiesPath(path);
		Agent agent = new Agent();
		agent.setUserMail(value);

		return agentUtil.updateAgentInfoFile(agent);
	}

	@Override
	public Setting viewSettingValue(String path) {
		agentUtil.setSettingPropertiesPath(path);

		return agentUtil.selectGrowthSettingFile();
	}

	@Override
	public boolean editSettingValue(Setting setting, String path) {
		agentUtil.setSettingPropertiesPath(path);

		return agentUtil.updateGrowthSettingFile(setting);
	}

}
