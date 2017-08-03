package client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.CharBuffer;

import org.slf4j.*;

import core.DirectoryModel;
import core.GlobalProperties;
import core.MessagePayload;
import mqpp_message_architecture.MQTTMessageClient;

public class ClientManager implements MqttCallback {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	MQTTMessageClient reciever;
	String topicNameUniqPrefix = GlobalProperties.defaultProps.getProperty("uniqKey", "bih1347813g");
	String updateTopicSuffix = GlobalProperties.defaultProps.getProperty("updateTopic", "updateTopic");
	String fullListTopicSuffix = GlobalProperties.defaultProps.getProperty("fullListTopic", "fullListTopic");

	String updateTopic = topicNameUniqPrefix + updateTopicSuffix;
	String fullListTopic = topicNameUniqPrefix + fullListTopicSuffix;

	int Qos = Integer.valueOf(GlobalProperties.defaultProps.getProperty("publishQoS", "1"));

	// private DirectoryModel model;

	public ClientManager() throws MqttException {
		reciever = new MQTTMessageClient();
		connect(false);		
	}
	

	private void connect(boolean preLost) throws MqttException{		
		reciever.setCallback(this);
		reciever.connect();
		reciever.subscribe(fullListTopic, Qos);
		reciever.subscribe(updateTopic, Qos);
	}

	@Override
	public void connectionLost(Throwable cause) {
		logger.warn("Connection lost {}", cause);
		throw new RuntimeException("Cant establish connection");
		/*try {
			connect(true);
		} catch (MqttException e) {			
			logger.warn("{}", e);
			
		}*/
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Message arrived topic:{}", topic);
		if (topic.equals(fullListTopic)) {
			if(MessagePayload.deserialize(message.getPayload())) {
				reciever.unSubscribe(fullListTopic);				
				ConsoleUI.instance.launch();
				MessagePayload.getModel().registerListener(ConsoleUI.instance);
				MessagePayload.fire();
			} else {
				throw new RuntimeException("DirectoryModel not initialized");
			}
		}
		if (topic.equals(updateTopic) && MessagePayload.getModel() != null) {
			MessagePayload.deserialize(message.getPayload());
			MessagePayload.fire();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// logger.info("Message delivered");
	}
}
