package mqpp_message_architecture;

import java.lang.invoke.MethodHandles;
import org.slf4j.*;

public class MQTTDefaultConfig {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String brockerAddres = "tcp://broker.hivemq.com:1883";
	public static final boolean memoryPersistance = true;
	
	public static final String willTopic = "LWT";
	public static final String willPayload = "LWTPayload";
	public static final int willQoS = 2;
	public static final boolean isWillRetained = false;
	
	public static final String username = "user";
	public static final String password = "pass";
	
	public static final String updateTopic = "testtopic20";
	public static final String fullListTopic = "testtopic30";
	public static final String publishPayload = "testPayload76";
	public static final int publishQoS = 2;
	public static final boolean isPublishRetained = true;
}
