package mqpp_message_architecture;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;

import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;

import org.slf4j.*;

import core.GlobalProperties;


public class MQTTMessageClient {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	MqttClient client;
	String ClientId;
	MqttClientPersistence persistance;
	MqttConnectOptions options = new MqttConnectOptions();
	

	public MQTTMessageClient(String brokerAddress) throws MqttException{		
		ClientId = MqttClient.generateClientId();
		String memoryPersistance = GlobalProperties.defaultProps.getProperty("memoryPersistance", "true");
		if (Boolean.getBoolean(memoryPersistance))
			persistance = new MemoryPersistence();
		else
			persistance = new MqttDefaultFilePersistence();
		
		client = new MqttClient(brokerAddress, ClientId, persistance);
		logger.info("MQTTClient started, clientId = {}, brokerAddress = {}", ClientId, brokerAddress);
	}
	
    public MQTTMessageClient() throws MqttException{    	
		this(GlobalProperties.defaultProps.getProperty("brockerAddres", "tcp://broker.hivemq.com:1883"));
	}

	public void setWill(){
		String topic = GlobalProperties.defaultProps.getProperty("uniqKey", "bih1347813g") + GlobalProperties.defaultProps.getProperty("LWTTopic", "LWTTopic");
		String payload = GlobalProperties.defaultProps.getProperty("LWTMessage", "ServerDown"); 
		int QoS = Integer.parseInt((GlobalProperties.defaultProps.getProperty("LWTQoS", "2"))); 
		boolean isRetained = Boolean.getBoolean(GlobalProperties.defaultProps.getProperty("LWTRetained", "true")); 
		setWill(topic, payload, QoS, isRetained);
	}
	public void setWill(String topic, String payload, int QoS, boolean isRetained) {
		try {
			options.setWill(topic, payload.getBytes("UTF-8"), QoS, isRetained);
		} catch (UnsupportedEncodingException e) {		
			logger.warn("{}", e);
		}
	}

	public void setUsernamePassword(){		
		String username = GlobalProperties.defaultProps.getProperty("username", "directory_observer");
		String password = GlobalProperties.defaultProps.getProperty("password", "observing_for_the_win");
		setUsernamePassword(username, password);
	}
	
	public void setUsernamePassword(String username, String password){
		options.setUserName(username);
		options.setPassword(password.toCharArray());
	}

	public boolean connect() throws MqttSecurityException, MqttException {
		logger.info("MQTTClient{} connecting to brocker = {}", ClientId, client.getServerURI());
		client.connect();
		logger.info("MQTTClient {} connection succesfull", ClientId);
		return true;// client.isConnected();
	}

	public void publish(String topic, byte[] payload, int Qos, boolean isRetained) throws MqttPersistenceException, MqttException {
		logger.info("MQTTClient-Sender {} publishing to topic = {} message = \"{}\"", ClientId, topic, payload);
		client.publish(topic, payload, Qos, isRetained);
	}

	public void setCallback(MqttCallback callback) {
		client.setCallback(callback);
	}
	
	public void subscribe(String topic, int QoS) throws MqttSecurityException, MqttException {			
		logger.info("MQTTClient-Reciever {} subscribing to topic {}", ClientId, topic);
		client.subscribe(topic, QoS);
	}
	
	public void unSubscribe(String topic) throws MqttException {			
		logger.info("MQTTClient-Reciever {} unSubscribeing from topic {}", ClientId, topic);
		client.unsubscribe(topic);
	}
}
