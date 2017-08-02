package server;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;

import core.DirectoryEvent;
import core.DirectoryEventListener;

public class ServerManager implements DirectoryEventListener{
	
	public ServerManager() {
		
	}
					

	@Override
	public void fileAdded(DirectoryEvent<HashMap<String, Object>> evt) {
		// TODO Auto-generated method stub
		System.out.println("added" + evt);
	}

	@Override
	public void fileDeleted(DirectoryEvent<String> evt) {
		// TODO Auto-generated method stub
		System.out.println("deleted" + evt);
	}

	@Override
	public void fileModified(DirectoryEvent<HashMap<String, Object>> evt) {
		// TODO Auto-generated method stub
		System.out.println("modified" + evt);
	}

	@Override
	public void initialized(DirectoryEvent<?> evt) {
		// TODO Auto-generated method stub
		System.out.println("initialized" + evt);
	}
}
