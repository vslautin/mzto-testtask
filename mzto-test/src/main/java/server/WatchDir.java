package server;

/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.DirectoryModel;
import core.FileAttrs;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir implements Runnable{
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
    private final WatchService watcher;    
    private final Path dir;    
    private DirectoryModel model;
    private Set<String> attrs;
    
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    
   

    /**
     * Creates a WatchService and registers the given directory
     * 
     * TODO: add consistency check for dir
     */        
    
    public WatchDir(DirectoryModel dm,  Set<String> strings) throws WatchDirException, IOException {
    	model = dm;     	  	   
    	this.dir = dm.getDirectory();    	
        this.watcher = FileSystems.getDefault().newWatchService();         
        model = dm;
        attrs = checkAttrs(strings);
        model.setFileAttributes(attrs);
        model.initialize(FileReader.readFiles(dir, attrs));
        dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY, OVERFLOW);
        
        
        logger.info(" WatchDir started for directory {} ", dir);
    }
        	
	
	private Set<String> checkAttrs(Set<String> strings) {
		HashSet<String> result = new HashSet<String>();
		for(String attr: strings)
			if(FileAttrs.fileAttrs.containsKey(attr)) 			
				result.add(attr);			
			else 
				logger.warn("{} is not acceptable FileAttribute name, ignored", attr);	
		return result;
	}	
    

    /**
     * Process all events for key queued to the watcher
     */    
	public void run() {
		while(true) {
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {                
                logger.info("WatchDir for path {} interrupted/stopped", dir);
                return;
            }
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                try {
                if (kind == OVERFLOW) {
                    model.addFiles(FileReader.readFiles(dir, attrs));
                }
                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path absName = dir.resolve(name);               
                if (kind == ENTRY_CREATE) {
                    model.addFile(name.toString(), FileReader.readFileAttrs(absName, attrs));
                }                
                if (kind == ENTRY_MODIFY) {
                    model.checkAndModify(name.toString(), FileReader.readFileAttrs(absName, attrs));
                }                
                if (kind == ENTRY_DELETE) {
                    model.deleteFile(name.toString());
                } 
                }
                catch(IOException e) {
                	logger.warn("{}", e);
                	Console console = System.console();              
                	char[] c;
                	while(true) {
                		c = console.readPassword("I/O error, Continue work? y - continue, n - abort");
                		if(c[0] == 'y') 
                		{            
                			break;
                		}
                		if(c[0] == 'n') {                			
                			return;
                		}
                	}
                }
            }
            key.reset();
        }
       
	}		
}