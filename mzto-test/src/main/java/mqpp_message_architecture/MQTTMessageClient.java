package mqpp_message_architecture;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;

import java.lang.invoke.MethodHandles;
import org.slf4j.*;

public class MQTTMessageClient{
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	MqttClient client;
	String ClientId;
	MqttClientPersistence persistance;
	MqttConnectOptions options = new MqttConnectOptions();
	
	public MQTTMessageClient()  throws MqttException{
		
		ClientId = MqttClient.generateClientId();	
		if(MQTTDefaultConfig.memoryPersistance)
			persistance = new MemoryPersistence();
		else
			persistance = new MqttDefaultFilePersistence();		
		client = new MqttClient(MQTTDefaultConfig.brockerAddres, ClientId, persistance); 
		logger.info("MQTTClient started, clientId = {}", ClientId);
	}
	
public void setWill() throws UnsupportedEncodingException {		 
	options.setWill( MQTTDefaultConfig.willTopic, MQTTDefaultConfig.willPayload.getBytes("UTF_8"), MQTTDefaultConfig.willQoS, MQTTDefaultConfig.isWillRetained);
}

public void setUsernamePassword() throws UnsupportedEncodingException {		 
	options.setUserName(MQTTDefaultConfig.username);
	options.setPassword(MQTTDefaultConfig.password.toCharArray());
}

	
	public boolean connect() throws MqttSecurityException, MqttException {
		logger.info("MQTTClient {} connecting to brocker = {}", ClientId, client.getServerURI());
		client.connect();
		logger.info("MQTTClient {} connection succesfull", ClientId);
		return true;//client.isConnected();
	}
	
	public void publish() throws MqttPersistenceException, UnsupportedEncodingException, MqttException {
		logger.info("MQTTClient {} publishing to topic = {} message = \"{}\"", ClientId, MQTTDefaultConfig.publishTopic, MQTTDefaultConfig.publishPayload);
		client.publish(MQTTDefaultConfig.publishTopic, MQTTDefaultConfig.publishPayload.getBytes("UTF-8"), MQTTDefaultConfig.publishQoS, MQTTDefaultConfig.isPublishRetained);
	}
	
	public void subscribe(MqttCallback callback) throws MqttSecurityException, MqttException {
		client.setCallback(callback);  
		logger.info("MQTTClient {} connecting to brocker = {}", ClientId, client.getServerURI());
		client.connect();
		logger.info("MQTTClient {} connection succesfull", ClientId);
		logger.info("MQTTClient {} subscribing to topic {}", ClientId, MQTTDefaultConfig.publishTopic);
		client.subscribe(MQTTDefaultConfig.publishTopic, 2);
	}
}
