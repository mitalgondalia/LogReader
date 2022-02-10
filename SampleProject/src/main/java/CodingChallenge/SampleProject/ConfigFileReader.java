package CodingChallenge.SampleProject;

import java.util.*;
import java.io.*;

public class ConfigFileReader {

	public static Properties p;

	public ConfigFileReader() throws IOException {
		FileReader reader = new FileReader(
				System.getProperty("user.dir") + "\\src\\test\\resources\\Configuration.properties");
		p = new Properties();
		p.load(reader);
	}

}