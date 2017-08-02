package mqpp_message_architecture;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.*;

public class MQTTStackLauncher {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	

	public static void main(String[] args) throws UnsupportedEncodingException {
		ClientManager manager = new ClientManager();
		try {
			MQTTMessageClient sender = new MQTTMessageClient();
			sender.connect();
			sender.publish();
			MQTTMessageClient reciever = new MQTTMessageClient();
			reciever.subscribe(manager);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			logger.warn("{}", e);
		}
		

	}
}
