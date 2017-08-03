package util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.slf4j.*;

public class BrokerAddressConfigGenerator {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public BrokerAddressConfigGenerator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		File f = new File("propertyBLABLA");
		f.createNewFile();
		DirectoryStream dirS;
		Consumer action = (s) -> System.out.println(s);
		String[] propertys = { System.getProperty("java.home"), System.getProperty("user.dir"),
				System.getProperty("user.home") };
		for (String s : propertys) {
			System.out.println(s);
			System.out.println("--------");
			dirS = Files.newDirectoryStream(Paths.get(s));
			dirS.forEach(action);
			System.out.println("--------");
		}

	}
}
