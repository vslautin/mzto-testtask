package core;

import java.util.EventListener;
import java.util.HashMap;

public interface DirectoryEventListener extends EventListener{
	public void fileAdded(DirectoryEvent<HashMap<String,Object>> evt);
	public void fileDeleted(DirectoryEvent<String> evt);
	public void fileModified(DirectoryEvent<HashMap<String,Object>> evt);
	public void initialized(DirectoryEvent<?> evt);
}
