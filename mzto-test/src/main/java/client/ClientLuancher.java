package client;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mqpp_message_architecture.MQTTMessageClient;

public class ClientLuancher {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	
	public static void main(String[] args) throws MqttException{
		new ClientManager();		
	}
}
