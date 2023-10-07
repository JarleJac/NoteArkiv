package jacJarSoft.noteArkiv.stresstest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
	private static Logger logger = Logger.getLogger(Main.class.getName());
	

	public static void main(String[] args) {
		try {
			logger.info("Starting test...");
			runTest();
			logger.info("test completed ok.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exeption during test: " + e.getMessage(), e);
		}
	}

	private static void runTest() throws Exception{
		Properties props = readProperties();
		TestController controller = new TestController(props);
		controller.runTest();
	}

	private static Properties readProperties() throws IOException, FileNotFoundException {
		String rootPath = System.getProperty("jacJarSoft.rootPath");
		if (rootPath == null)
			rootPath = getJarPath();
		File propFile = Paths.get(rootPath,"stresstest.properties").toFile();
		Properties props = new Properties();
		try (FileInputStream is = new FileInputStream(propFile)) {
			props.load(is);
		}
		return props;
	}


	private static String getJarPath() {
		try {
			String path = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			logger.fine("path: " + path);
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			logger.fine("decodedPath: " + decodedPath);
			String retPath = decodedPath.substring(0, decodedPath.lastIndexOf("\\"));
			logger.fine("retPath: " + retPath);
			return retPath;
		} catch (UnsupportedEncodingException | URISyntaxException e) {
			throw new RuntimeException("Unable to get jar path", e);
		}
	}

}
