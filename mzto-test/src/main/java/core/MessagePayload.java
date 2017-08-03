package core;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.slf4j.*;


public class MessagePayload {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static byte[] makePayload(DirectoryEvent e) throws IOException {
		byte[] result;
		result = serialize(e);
		return result;
	}

	public static byte[] makePayload(String dir, HashMap<String, HashMap<String, Object>> files) throws IOException {
		byte[] result;
		Object[] o = {dir, files};
		result = serialize(o);
		return result;
	}
	
	private static byte[] serialize(Object o) throws IOException  {
		byte[] result;
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(byteOutput)) {

			out.writeObject(o);
			//out.writeObject(Object.class);
			out.close();
			result = byteOutput.toByteArray();
		} 
		result = byteOutput.toByteArray();
		try {
			byteOutput.close();
		}
		catch(IOException e) {
			logger.warn("Failed to close stream {}", byteOutput);
		}
		return result;
	}

	public static boolean deserialize(byte[] payload) throws IOException, ClassNotFoundException{
		ByteArrayInputStream byteInput = new ByteArrayInputStream(payload);
		try(ObjectInputStream objectInput = new ObjectInputStream(byteInput)){
			Object firstObject = objectInput.readObject();
			Class<?> c = firstObject.getClass();
			logger.info("Deserialized object. Class : {} Object : {}", c, firstObject);
			model = new DirectoryModel();
			//if(firstObject.getClass() == HashMap.class) 
			return true;
		}
	}
		
	public static void fire() {
		
	}
	
	private static DirectoryModel model;
	public static DirectoryModel getModel() {
		return model;
	}
	
}
