package core;

import java.lang.invoke.MethodHandles;
import java.util.EventObject;

import org.slf4j.*;

public class DirectoryEvent<C> extends EventObject {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1638508764697207589L;
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());	
	private C context;
	private String file;
	
	public DirectoryEvent(Object source,String file, C context) {
		super(source);		
		this.context = context;
		this.file = file;
		logger.info("created {}", this.toString());
	}
	
	public DirectoryEvent(Object source, String file) {
		super(source);				
		this.file = file;
		logger.info("created {}", this.toString());
	}
	
	public DirectoryEvent(C source) {
		super(source);				
		logger.info("created {}", this.toString());
	}

	public C getContext() {
		return context;
	}

	@Override
	public String toString() {
		return "DirectoryEvent [context=" + context + ", file=" + file + ", source=" + source + "]";
	}

			
	
	
}
