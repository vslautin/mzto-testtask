package core;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;

import org.slf4j.*;

public class DirectoryModel {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private Set<String> attrsToCheck = new HashSet<String>();
	private HashMap<String,HashMap<String,Object>> files = new HashMap<String,HashMap<String, Object>>();
	public Path directory; 	
	private Set<DirectoryEventListener> listeners = new HashSet<DirectoryEventListener>();
	
	
	public DirectoryModel() {		
	}
	
	public DirectoryModel(Path directory) {		
		this.directory = directory;	
		logger.info("DirectoryModel for {} created", directory);
	}		
	
	
	public void setFileAttributes(Set<String> strings) {	
		attrsToCheck = strings;
		logger.info("Attributes set : {}", strings);
	}
	
	public Set<String> getAttrsToCheck() {
		return attrsToCheck;
	}

	public HashMap<String, HashMap<String, Object>> getFiles() {
		return files;
	}

	public Path getDirectory() {
		return directory;
	}
	
	public void initialize(HashMap<String,HashMap<String, Object>> files) {				       
		this.files = files;
		logger.info("DirectoryModel is initialized by files : {}", files);
		fireInitializeEvent();
	}	
	
	public void addFiles(HashMap<String,HashMap<String, Object>> currentFiles) {			
		Set<String> removedFiles = files.keySet();
		removedFiles.removeAll(currentFiles.keySet());
		
		if(removedFiles.size()>0)
			for(String removedFile : removedFiles)
				deleteFile(removedFile);
		
		Set<String> newFiles = currentFiles.keySet();
			newFiles.removeAll(files.keySet());
		
		if(newFiles.size()>0)
			for(String newFile : newFiles)
				addFile(newFile, currentFiles.get(newFile));
	}		
	
	
	public void deleteFile(String str) {
		if(!files.containsKey(str)) {
			logger.error("removed file was already removed");			
		}
		else{
			files.remove(str);
			fireDeleteEvent(str);
		}
		
	}
	public void addFile(String name, HashMap<String,Object> attrs) {
		Object result = files.putIfAbsent(name, attrs);
		if(result!=null) {
			logger.error("added file was already in model");			
		}
		else {
			fireAddEvent(name);
		}
	}
	public void checkAndModify(String file, HashMap<String, Object> currentAttrs) {
		Map<String, Object> oldAttrs = files.get(file);
		HashMap<String, Object> newAttrs = new HashMap<String, Object>();
		Object oldValue;
		Object newValue;
		for(String attr: oldAttrs.keySet()) {
			oldValue = oldAttrs.get(attr);
			newValue = currentAttrs.get(attr);
			if(!oldValue.equals(newValue))
				newAttrs.put(attr, newValue);
		}
		if(newAttrs.size()>0)
			modify(file, newAttrs);
	}
	public void modify(String file, HashMap<String, Object> newAttrs) {
		Map<String, Object> currentAttrs = files.get(file);
		for(String newAttr: newAttrs.keySet()) {
			currentAttrs.put(newAttr, newAttrs.get(newAttr));
		}
		fireModifyEvent(file, newAttrs);
	}
	
	private void fireInitializeEvent() {
		DirectoryEvent<HashMap<String,HashMap<String,Object>>> event = new DirectoryEvent<HashMap<String,HashMap<String,Object>>>(this, DirectoryEvent.EventType.INIT , files);
		for(DirectoryEventListener l : listeners)
			l.initialized(event);
	}
	private void fireAddEvent(String name) {
		DirectoryEvent<HashMap<String,Object>> event = new DirectoryEvent<HashMap<String,Object>>(this, DirectoryEvent.EventType.ADD, name, files.get(name));
		for(DirectoryEventListener l : listeners)
			l.fileAdded(event);
	}
	private void fireDeleteEvent(String name) {
		DirectoryEvent<String> event = new DirectoryEvent<String>(this, DirectoryEvent.EventType.DEL, name);
		for(DirectoryEventListener l : listeners)
			l.fileDeleted(event);
	}
	private void fireModifyEvent(String name,HashMap<String,Object> modifiedAttrs ) {
		DirectoryEvent<HashMap<String,Object>> event = new DirectoryEvent<HashMap<String,Object>>(this, DirectoryEvent.EventType.MOD, name, modifiedAttrs);
		for(DirectoryEventListener l : listeners)
			l.fileModified(event);
	}
	

	public void registerListener(DirectoryEventListener l) {
		listeners.add(l);
	}

	public void removeListener(DirectoryEventListener l) {
		listeners.remove(l);
	}	
	
	}
