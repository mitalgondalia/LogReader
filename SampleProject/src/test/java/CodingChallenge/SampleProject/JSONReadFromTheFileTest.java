package CodingChallenge.SampleProject;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class JSONReadFromTheFileTest {
	private static final Logger logger = Logger.getLogger(JSONReadFromTheFileTest.class.getName());

	public static void main(String[] args) throws IOException {
		ConfigFileReader config = new ConfigFileReader();
		try {
			String fileName;
			if (args.length == 0)  //if input file is not provided, use default value from property file
				fileName = System.getProperty("user.dir") + ConfigFileReader.p.getProperty("filePath");
			else
				fileName = args[0];
			logger.info("Processing file " + fileName);
			File file = new File(fileName);
			if (file.exists()) {
				
				LogProcessor objProcessor = new LogProcessor();

				objProcessor.ProcessFile(file);
			} else {
				logger.error("invalid input file: " + fileName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
