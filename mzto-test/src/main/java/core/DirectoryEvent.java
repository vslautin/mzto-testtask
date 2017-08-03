package core;

import java.io.Serializable;
import java.util.EventObject;

import java.lang.invoke.MethodHandles;
import org.slf4j.*;

public class DirectoryEvent<C> extends EventObject implements Serializable{	
	public static enum EventType {
		INIT, ADD, DEL, MOD 
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1638508764697207589L;
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());	
	private C context;
	private String file;	
	private EventType eventType;
	
	public DirectoryEvent(Object source, EventType eventType, String file, C context) {
		super(source);		
		this.context = context;
		this.file = file;
		this.eventType = eventType;
		logger.info("created {}", this.toString());
	}
	
	/*public DirectoryEvent(Object source, EventType eventType, String file) {
		super(source);				
		this.file = file;
		this.eventType = eventType;
		logger.info("created {}", this.toString());
	}*/
	
	public DirectoryEvent(Object source, EventType eventType, C context) {
		super(source);	
		this.eventType = eventType;
		logger.info("created {}", this.toString());
	}

	public C getContext() {
		return context;
	}
	
	public String getFile() {
		return file;
	}

	public EventType getEventType() {
		return eventType;
	}

	@Override
	public String toString() {
		return "DirectoryEvent [context=" + context + ", file=" + file + ", source=" + source + "]";
	}

			
	
	
}
