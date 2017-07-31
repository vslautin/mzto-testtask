package slautin.homework.mzto_test.server;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.*;

public class ServerLauncher {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	//final static Logger logger = LoggerFactory.getLogger(ServerLauncher.class);
	
	
	public static void main(String[] args) throws IOException {
        // parse arguments
        if (args.length > 1)
            usage();                
        // register directory and process its events
        Path dir = Paths.get(args[0]);
        logger.info("starting WatchDir for directory {}", dir);
        //new WatchDir(dir, recursive).processEvents();
    }
	
	private static void usage() {
		logger.warn("usage: java WatchDir dir"); 
        System.exit(-1);
    }
}
