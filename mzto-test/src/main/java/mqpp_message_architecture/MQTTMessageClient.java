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
		if(MQTTConfig.memoryPersistance)
			persistance = new MemoryPersistence();
		else
			persistance = new MqttDefaultFilePersistence();		
		client = new MqttClient(MQTTConfig.brockerAddres, ClientId, persistance); 
		logger.info("MQTTClient started, clientId = {}", ClientId);
	}
	
public void setWill() throws UnsupportedEncodingException {		 
	options.setWill( MQTTConfig.willTopic, MQTTConfig.willPayload.getBytes("UTF_8"), MQTTConfig.willQoS, MQTTConfig.isWillRetained);
}

public void setUsernamePassword() throws UnsupportedEncodingException {		 
	options.setUserName(MQTTConfig.username);
	options.setPassword(MQTTConfig.password.toCharArray());
}

	
	public boolean connect() throws MqttSecurityException, MqttException {
		logger.info("MQTTClient {} connecting to brocker = {}", ClientId, client.getServerURI());
		client.connect();
		logger.info("MQTTClient {} connection succesfull", ClientId);
		return true;//client.isConnected();
	}
	
	public void publish() throws MqttPersistenceException, UnsupportedEncodingException, MqttException {
		logger.info("MQTTClient {} publishing message to topic = {}", MQTTConfig.publishTopic);
		client.publish(MQTTConfig.publishTopic, MQTTConfig.publishPayload.getBytes("UTF-8"), MQTTConfig.publishQoS, MQTTConfig.isPublishRetained);
	}
	
	public void subscribe(MqttCallback callback) throws MqttSecurityException, MqttException {
		client.setCallback(callback);  
		logger.info("MQTTClient {} connecting to brocker = {}", ClientId, client.getServerURI());
		client.connect();
		logger.info("MQTTClient {} connection succesfull", ClientId);
		logger.info("MQTTClient {} subscribing to topic {}", ClientId, MQTTConfig.publishTopic);
		client.subscribe(MQTTConfig.publishTopic, 2);
	}
}
