package mqpp_message_architecture;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.CharBuffer;

import org.slf4j.*;

import core.DirectoryModel;


public class ClientManager implements MqttCallback {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private DirectoryModel model;
	
	
	public ClientManager() {
		
	}
	
	public ClientManager(DirectoryModel model) {
		this.model =  model;
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		logger.warn("Connection lost {}", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message.getPayload())));
		String StringMessage = reader.readLine();
		logger.info("Message arrived topic:{} , message: \"{}\"", topic, StringMessage);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		logger.info("Message delivered");
	}
}
