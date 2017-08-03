package server;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;

import core.DirectoryEvent;
import core.DirectoryEventListener;
import core.DirectoryModel;
import core.GlobalProperties;
import core.MessagePayload;
import mqpp_message_architecture.MQTTMessageClient;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.slf4j.*;

import ch.qos.logback.core.util.SystemInfo;

public class ServerManager implements DirectoryEventListener {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	MQTTMessageClient mqttClient;
	String topicNameUniqPrefix = GlobalProperties.defaultProps.getProperty("uniqKey", "bih1347813g");
	String updateTopicSuffix = GlobalProperties.defaultProps.getProperty("updateTopic", "updateTopic");
	String fullListTopicSuffix = GlobalProperties.defaultProps.getProperty("fullListTopic", "fullListTopic");

	String updateTopic = topicNameUniqPrefix + updateTopicSuffix;
	String fullListTopic = topicNameUniqPrefix + fullListTopicSuffix;

	int Qos = Integer.valueOf(GlobalProperties.defaultProps.getProperty("publishQoS", "1"));

	public ServerManager() {
		while (true)
			try {
				mqttClient = new MQTTMessageClient();
				mqttClient.setWill();
				mqttClient.connect();
				return;
				
			} catch (MqttException e) {
				logger.error("{}", e);
				System.out.println("retry? anykey to retry, n - exit");
				try (InputStreamReader r = new InputStreamReader(System.in)) {
					if (r.read() == 'n')
						return;
					else
						continue;
				} catch (IOException ex) {
					logger.warn("Cant read from System in. Exit");
					return;
				}
			}
	}

	@Override
	public void fileAdded(DirectoryEvent<HashMap<String, Object>> evt) {
		logger.info("ServerManager get event \"added\" {}", evt);
		createPayloadAndPublish(evt);
	}

	@Override
	public void fileDeleted(DirectoryEvent<String> evt) {
		logger.info("ServerManager get event \"deleted\" {}", evt);
		createPayloadAndPublish(evt);
	}

	@Override
	public void fileModified(DirectoryEvent<HashMap<String, Object>> evt) {
		logger.info("ServerManager get event \"modified\" {}", evt);
		createPayloadAndPublish(evt);
	}

	@Override
	public void initialized(DirectoryEvent<?> evt) {
		logger.info("ServerManager get event \"initialized\" {}", evt);
		createPayloadAndPublish(evt);
	}

	private void createPayloadAndPublish(DirectoryEvent evt) {
		byte[] updatePayload;
		byte[] fullListPayload;
		DirectoryModel model;

		try {
			updatePayload = MessagePayload.makePayload(evt);
			mqttClient.publish(updateTopic, updatePayload, Qos, false);

			model = (DirectoryModel) evt.getSource();
			fullListPayload = MessagePayload.makePayload(model.getDirectory().toString(), model.getFiles());
			mqttClient.publish(fullListTopic, fullListPayload, Qos, true);

		} catch (IOException e) {
			logger.error("Message creation faild {}", e);
		} catch (MqttPersistenceException e) {
			logger.error("Message publishing failed {}", e);
		} catch (MqttException e) {
			logger.error("Message publishing failed {}", e);
		}
	}
}
