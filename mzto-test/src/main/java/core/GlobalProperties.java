package core;

import org.slf4j.*;


import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class GlobalProperties {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static Properties defaultProps;
	public static Set<String> fileAttributes = new HashSet<String>();
	static {
		defaultProps = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream("src\\project.properties");	
			defaultProps.load(in);
			in.close();
		} catch (FileNotFoundException e) {			
			logger.warn("{}", e);			
		} catch (IOException e) {			
			logger.warn("{}", e);
		}		
		if(defaultProps == null)
			defaultProps = new Properties();
		try {			
			Scanner scanner = new Scanner((new FileReader("src\\file.attributes")));
			scanner = scanner.useDelimiter("\\s");
			while(scanner.hasNext())
				fileAttributes.add(scanner.next());
			scanner.close();				
		} catch (FileNotFoundException e) {			
			logger.warn("{}", e);			
		} catch (IOException e) {			
			logger.warn("{}", e);
		}
	}	
	
	public static void main(String[] args) {
		System.out.println(fileAttributes);
	}
}
