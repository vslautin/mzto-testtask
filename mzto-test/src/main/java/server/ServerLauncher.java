package server;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.slf4j.*;

import core.*;

public class ServerLauncher {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	//final static Logger logger = LoggerFactory.getLogger(ServerLauncher.class);
	
	
	public static void main(String[] args) throws IOException, WatchDirException {
        // parse arguments
        if (args.length < 1)
            usage();                
        // register directory and process its events      
        Path dir = Paths.get(args[0]);        
        logger.info("starting WatchDir for directory {}", dir);
        DirectoryModel dm = new DirectoryModel(dir);
        ServerManager manager = new ServerManager();
        dm.registerListener(manager);
        
        Set<String> s = new HashSet<String>();
        for(int i=1;i<args.length;i++)
        	s.add(args[i]);
        	
        WatchDir wd = new WatchDir(dm, dm.getDirectory(), s);
        Thread t = new Thread(wd);
        t.start(); 
    }
	
	private static void usage() {
		logger.warn("usage: java directory[ attributeToWotch]*"); 
        System.exit(-1);
    }
}
