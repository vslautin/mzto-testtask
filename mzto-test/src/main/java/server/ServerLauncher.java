package server;


import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.slf4j.*;
import core.*;
import mqpp_message_architecture.MQTTMessageClient;


public class ServerLauncher {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	//final static Logger logger = LoggerFactory.getLogger(ServerLauncher.class);
	
	
	
	public static void main(String[] args) throws IOException, WatchDirException {
        // parse arguments
        if (args.length < 1 || args.length > 1)
            usage();                
        // register directory and process its events      
        Path dir = Paths.get(args[0]);                     	
        dir = checkPath(dir);
                               
        ServerManager manager = new ServerManager();      
        DirectoryModel dm = new DirectoryModel(dir);
        dm.registerListener(manager);
        
        Set<String> fileAttributes = GlobalProperties.fileAttributes;
        
        logger.info("starting WatchDir for directory {}", dir);
        WatchDir wd = new WatchDir(dm, fileAttributes);
        Thread t = new Thread(wd);
        t.start(); 
    }
	
	private static Path checkPath(Path dir) throws IllegalArgumentException {
		if(dir.isAbsolute()) {
			if(dir.toFile().isDirectory())
				return dir;
			else throw new IllegalArgumentException("dir is absolute path and isnt directory");
		}		
		Path result = dir.toAbsolutePath();
		if(result.toFile().isDirectory())	
			return result;
		result = Paths.get(System.getProperty("user.dir")).resolve(dir);
		if(result.toFile().isDirectory())	
			return result;
		else
			throw new IllegalArgumentException("dir is absolute path and isnt directory");
	}
	
	private static void usage() {
		logger.warn("usage: java directory[ attributeToWotch]*"); 
        System.exit(-1);
    }
}
