package kr.co.smf.agent.management;

import java.util.Properties;
import java.util.Calendar;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.io.FileReader;
import java.lang.Math;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

import kr.co.smf.agent.beans.Agent;
import kr.co.smf.agent.beans.Measurement;
import kr.co.smf.agent.beans.Setting;
import kr.co.smf.agent.util.AgentUtil;

public class GrowthManagementServiceImple implements GrowthManagementService {
	AgentUtil agentUtil = new AgentUtil();
	
	private static String destination;
	private static String user;
	private static String password;

	@Override
	public void manageGrowth() {
		GpioController controller = GpioFactory.getInstance();
		GpioPinDigitalOutput pin = controller.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);

		int PIN_NO = 7;
		int MAX_TIMINGS = 85;
		int[] dataSet = { 0, 0, 0, 0, 0 };
		
		int status = Gpio.HIGH;

		dataSet[0] = 0;
		dataSet[1] = 0;
		dataSet[2] = 0;
		dataSet[3] = 0;
		dataSet[4] = 0;

		Gpio.pinMode(PIN_NO, Gpio.OUTPUT);
		Gpio.digitalWrite(PIN_NO, Gpio.LOW);
		Gpio.delay(18);

		Gpio.digitalWrite(PIN_NO, Gpio.HIGH);
		Gpio.pinMode(PIN_NO, Gpio.INPUT);

		int j = 0;

		for (int i = 0; i < 85; i++) {
			int count = 0;
			while (Gpio.digitalRead(PIN_NO) == status) {
				count++;

				Gpio.delayMicroseconds(2);
				if (count == 255) {
					break;
				}
			}

			status = Gpio.digitalRead(PIN_NO);
			if (count == 255) {
				break;
			}

			if ((i >= 4) && (i % 2 == 0)) {
				dataSet[j / 8] <<= 1;
				if (count > 16) {
					dataSet[j / 8] |= 1;
				}

				j++;
			}
		}
		
		controller.shutdown();
		controller.unprovisionPin(pin);
		
		if (true) {
			float h = (float) ((dataSet[0] << 8) + dataSet[1]) / 10;
			if (h > 100) {
				h = dataSet[0];
			}

			float c = (float) (((dataSet[2] & 0x7F) << 8) + dataSet[3]) / 10;
			if (c > 125) {
				c = dataSet[2];
			}

			if ((dataSet[2] & 0x80) != 0) {
				c = -c;
			}

			float f = c * 1.8f + 32;
			
			Measurement measurement = new Measurement();
			measurement.setTemperature(Math.round(c*10)/10.0);
			measurement.setHumidity((int)h);
			measurement.setCo2(2000);
			agentUtil.updateGrowthMeasurementFile(measurement);
			
			Setting setting = agentUtil.selectGrowthSettingFile();

			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int date = calendar.get(Calendar.DATE);
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);

			System.out.println("{ { \"T\" : \"" + year + "-" + String.format("%02d", month) + "-"
					+ String.format("%02d", date) + " " + String.format("%02d", hour) + ":"
					+ String.format("%02d", minute) + ":" + String.format("%02d", second) + "\", \"H\" : \"" + h
					+ "\", \"C\" : \"" + c);
					
			if(Math.abs(h - setting.getHumidity()) >= 3){
				notifyEmergency(setting, measurement);
			} else if(Math.abs(c - setting.getTemperature()) >= 3) {
				notifyEmergency(setting, measurement);
			} else if(Math.abs(2000 - setting.getCo2()) >= 300) {
				notifyEmergency(setting, measurement);
			}
			
					
			if(h > setting.getHumidity()) {
				controlFan(true);
				System.out.println("h fan on");
				controlHeater(false);
				System.out.println("h heaterFan off");
			} else if (h == setting.getHumidity()) {
				controlHeater(false);
				System.out.println("h heaterFan off");
				controlFan(false);
				System.out.println("h fan off");
				
				if(c > setting.getTemperature()) {
					controlHeater(false);
					System.out.println("c heaterFan off");
					controlFan(true);
					System.out.println("c fan on");
				} else if (c == setting.getTemperature()) {
					controlFan(false);
					System.out.println("c fan off");
					controlHeater(false);
					System.out.println("c heaterFan off");
				} else if(c < setting.getTemperature()){
					controlFan(false);
					System.out.println("c fan off");
					controlHeater(true);
					System.out.println("c heaterFan on");
				}
			} else if(h < setting.getHumidity()){
				controlFan(false);
				System.out.println("h fan off");
				controlHeater(false);
				System.out.println("h heaterFan off");
			}
			
		} else {
			System.out.println("Unreliable data.");
		}
	}

	@Override
	public void controlFan(boolean state) {
		GpioController controller = GpioFactory.getInstance();
		GpioPinDigitalOutput pin4 = controller.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
		GpioPinDigitalOutput pin5 = controller.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
		GpioPinDigitalOutput pin1 = controller.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
		
		if(state == true) {
			pin4.low();
			pin5.low();
			pin1.high();
			controller.shutdown();
			controller.unprovisionPin(pin4);
			controller.unprovisionPin(pin5);
			controller.unprovisionPin(pin1);
		} else {
			pin4.high();
			pin5.high();
			pin1.low();
			controller.shutdown();
			controller.unprovisionPin(pin4);
			controller.unprovisionPin(pin5);
			controller.unprovisionPin(pin1);
		}
	}

	@Override
	public void controlHeater(boolean state) {
		GpioController controller = GpioFactory.getInstance();
		GpioPinDigitalOutput fan = controller.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW);
		GpioPinDigitalOutput heater = controller.provisionDigitalOutputPin(RaspiPin.GPIO_25, PinState.LOW);
		heater.low();
		
		if(state ==true) {
			fan.low();
		} else {
				fan.high();
			}
		
		controller.shutdown();
		controller.unprovisionPin(fan);
		controller.unprovisionPin(heater);
	}

	@Override
	public boolean notifyEmergency(Setting setting, Measurement measurement) {
		Agent agent = agentUtil.selectAgentInfoFile();
		String userMail = agent.getUserMail();
		
		FileReader fileReader = null;
		Properties properties = new Properties();
		
		// 1. ???????????? ?????? ????????? ???????????? ??????
		try {
			fileReader = new FileReader(new File("/home/mybatis/Desktop/scheduler/mail.properties"));
			properties.load(fileReader);

			destination = properties.getProperty("destination");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		// 2. Property??? SMTP ?????? ?????? ??????
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		// 3. SMTP ??????????????? ????????? ????????? ???????????? Session ???????????? ???????????? ??????
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		// 4. Message ???????????? ????????? ???????????? ???????????? ??????, ????????? ???????????? ????????????.
		// 5. Transport ???????????? ???????????? ????????? ???????????? ????????????.
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			// ?????????????????????
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userMail));
			// Subject
			message.setSubject("???????????? ?????? ?????? ?????????"); // ?????? ????????? ??????
			// Text
			message.setText("????????? ??????????????? ????????? ?????????????????????.\n" 
			+ "?????? ????????? ???????????? --> \t"+"[??????]\t" + measurement.getTemperature()+"???\t"+ " [??????]\t" + measurement.getHumidity()+"%\t"+" [co2]\t" +measurement.getCo2()+"ppm"+"\n"
			+ "????????? ???????????? ?????? --> \t"+"[??????]\t" + setting.getTemperature()+"???\t"+ " [??????]\t" + setting.getHumidity()+"%\t"+" [co2]\t" +setting.getCo2()+"ppm"); // ?????? ????????? ??????
			// send the message
			Transport.send(message); //// ??????
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
	public boolean changeDestination(String value) {
		Agent agent = new Agent();
		agent.setUserMail(value);

		return agentUtil.updateAgentInfoFile(agent);
	}

	@Override
	public Setting viewSettingValue() {
		return agentUtil.selectGrowthSettingFile();
	}

	@Override
	public boolean editSettingValue(Setting setting) {
		return agentUtil.updateGrowthSettingFile(setting);
	}

}
