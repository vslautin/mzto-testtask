package client;

import java.util.*;

import core.*;

public class ConsoleUI implements DirectoryEventListener {
	
	public static final ConsoleUI instance = new ConsoleUI();
	
    public void launch(){
		System.out.println("Console Launched\\n");
		
		System.out.println("-------------------------------------------------------------------");
	}

	@Override
	public void fileAdded(DirectoryEvent<HashMap<String, Object>> evt) {
		System.out.println("File " + evt.getFile() + " added");
		System.out.println("-------------------------------------------------------------------");
		repaint(evt);

	}

	@Override
	public void fileDeleted(DirectoryEvent<String> evt) {
		System.out.println("File " + evt.getFile() + " deleted");
		System.out.println("-------------------------------------------------------------------");
		repaint(evt);
	}

	@Override
	public void fileModified(DirectoryEvent<HashMap<String, Object>> evt) {
		System.out.println("File " + evt.getFile() + " modified");
		System.out.println("-------------------------------------------------------------------");
		repaint(evt);
	}

	@Override
	public void initialized(DirectoryEvent<?> evt) {
		System.out.println("New Dir initialized");
		System.out.println("-------------------------------------------------------------------");
		repaint(evt);
	}

	private void repaint(DirectoryEvent<?> evt) {
		List<String> attrsSorted = new LinkedList<String>();
		((DirectoryModel) evt.getSource()).getAttrsToCheck().forEach((s) -> attrsSorted.add(s));
		HashMap<String, HashMap<String, Object>> files = ((DirectoryModel) evt.getSource()).getFiles();
		Collections.sort(attrsSorted);
		StringBuilder sb = new StringBuilder();
		for (String file : files.keySet()) {
			for (String attr : attrsSorted)
				sb.append(" ").append(files.get(file).get(attr));
			System.out.println(file + sb);
		}
	}
}
