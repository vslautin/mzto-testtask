package mqpp_message_architecture;

import java.lang.invoke.MethodHandles;
import org.slf4j.*;

public class BrokerAddress {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String address;
	private int[] ports;
	private int[] SSLports;
	
	public BrokerAddress(String address, int[] ports, int[] SSLports) {
		super();
		this.address = address;
		this.ports = ports;
		this.SSLports = SSLports;
	}

	public String getAddress() {
		return address;
	}

	public int[] getPorts() {
		return ports;
	}

	public int[] getSSLports() {
		return SSLports;
	}
	
	
}
