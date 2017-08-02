package server;

import java.lang.invoke.MethodHandles;
import org.slf4j.*;

public class WatchDirException extends Exception {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public WatchDirException() {
		// TODO Auto-generated constructor stub
	}

	public WatchDirException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public WatchDirException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public WatchDirException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public WatchDirException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
}
