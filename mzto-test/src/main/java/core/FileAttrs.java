package core;

import java.awt.List;
import java.lang.invoke.MethodHandles;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.*;

public class FileAttrs {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static final Map<String,Class<?>> fileAttrs= new HashMap<String,Class<?>>();
	
	
	
	static FileSystem fileSystem = FileSystems.getDefault();
	public static final Set<String> supportedViews = fileSystem.supportedFileAttributeViews();
	
	public static final String basicAttrsViewName = "basic";
	public static final String dosAttrsViewName = "dos";
	public static final String posixAttrsViewName = "posix";
	public static final String aclAttrsViewName = "acl";
	
	static {
		
		fileAttrs.put(basicAttrsViewName + ":" + "lastModifiedTime",	FileTime.class);
		fileAttrs.put(basicAttrsViewName + ":" + "lastAccessTime",	FileTime.class);
		fileAttrs.put(basicAttrsViewName + ":" + "creationTime",	FileTime.class);
		fileAttrs.put(basicAttrsViewName + ":" + "size",	Long.class);
		fileAttrs.put(basicAttrsViewName + ":" + "isRegularFile",	Boolean.class);
		fileAttrs.put(basicAttrsViewName + ":" + "isDirectory",	Boolean.class);
		fileAttrs.put(basicAttrsViewName + ":" + "isSymbolicLink",	Boolean.class);
		fileAttrs.put(basicAttrsViewName + ":" + "lastModifiedTime",	FileTime.class);
		fileAttrs.put(basicAttrsViewName + ":" + "isOther",	Boolean.class);
		
		fileAttrs.put(dosAttrsViewName + ":" + "archive",	Boolean.class);
		fileAttrs.put(dosAttrsViewName + ":" + "hidden",	Boolean.class);
		fileAttrs.put(dosAttrsViewName + ":" + "readonly",	Boolean.class);		
		fileAttrs.put(dosAttrsViewName + ":" + "system",	Boolean.class);
		
				
		fileAttrs.put(posixAttrsViewName + ":" + "permissions",	Set.class); //TODO: Set<PosixFilePermission>
		fileAttrs.put(posixAttrsViewName + ":" + "group",	GroupPrincipal.class);
		
		fileAttrs.put(aclAttrsViewName + ":" + "acl",	List.class); //TODO: List<AclEntry>
		fileAttrs.put(aclAttrsViewName + ":" + "owner",	UserPrincipal.class);		
				
	}
					
		
}