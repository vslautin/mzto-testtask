package server;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.*;

/*
 * Class to separate i / o functionality from DirectoryModel. Mainly for testing purposes
 */
public class FileReader {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static boolean toFollowLinks = false;	
	//public static HashMap<String,HashMap<String,Object>> files;
	
	public static HashMap<String,HashMap<String,Object>> readFiles(Path directory, Set<String> attributes) throws IOException{
		HashMap<String,HashMap<String,Object>> files = new HashMap<String,HashMap<String, Object>>();									
		HashMap<String,Object> currentAttrs;
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)){
			for (Path file: dirStream) { 
				currentAttrs = readFileAttrs(file, attributes);				
				files.put(file.getFileName().toString(), currentAttrs);
			}
		}		
		return files;
	}
	
	public static HashMap<String,Object> readFileAttrs(Path absPath, Set<String> attributes) throws IOException{
		HashMap<String,Object> newAttrs = new HashMap<String,Object>();				
		for(String attribute : attributes) {
			newAttrs.put(attribute, 
					Files.getAttribute(absPath, 
							attribute, toFollowLinks?null:LinkOption.NOFOLLOW_LINKS));	
							// java.nio.file.Files.readAttributes(Path path, String attributes, LinkOption... options) throws IOException
							// other approach					
		}
		return newAttrs;
	}
	
	public static void main(String[] args) throws IOException {
		Set<String> attrs = new HashSet<String>();
		attrs.add("basic:isDirectory");		
		HashMap<String,HashMap<String,Object>> result = readFiles(Paths.get("D:\\Fraps"), attrs);
		for(String file: result.keySet()) {
			System.out.printf("%1$-15s %2$15s \n", file, result.get(file));			
		}
			
	}
}
