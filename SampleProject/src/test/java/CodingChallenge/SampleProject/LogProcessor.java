package CodingChallenge.SampleProject;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

public class LogProcessor implements Serializable {

	private static final long serialVersionUID = 1L; // for further change to implement serialization /
														// de-serialization.
	private static final Logger logger = Logger.getLogger(LogProcessor.class.getName());

	HashMap<String, LogProcessInfo> map = new HashMap<>();
	private static int AlertMs = Integer.parseInt(ConfigFileReader.p.getProperty("alertMs", "4"));
	private static int BatchSize = Integer.parseInt(ConfigFileReader.p.getProperty("batchSaveSize"));
	long lineCounter = 0;

	/*
	 * Input file name reads file line by line creates LogProcessorInfo class with
	 * data to be persisted If for an eventId, STARTED and FINISHED timestamp is
	 * present in LogProcessorInfo class, it would assume that processing for the
	 * event is done and it will be mark as ready for save. while processing the
	 * file, once the HashMap has 500 records, it would save the processed records
	 * to db and remove those objects from the hashmap.
	 */
	@SuppressWarnings("unchecked")
	public void ProcessFile(File file)  throws Exception {
		BasicConfigurator.configure();
		

		//File file = new File(fileName);

		ObjectMapper mapper = new ObjectMapper();
		try (Stream<String> linesStream = Files.lines(file.toPath())) {

			linesStream.forEach(line -> {
				lineCounter++;
				if (line != null && ((String) line).length() > 0) { // we could add more validations here.

					try {

						LogInfo objl = mapper.readValue((String) line, LogInfo.class);

						logger.debug(lineCounter + " - " + objl.id + ":" + objl.host + ":" + objl.timestamp);
						if (map.containsKey(objl.id)) {
							LogProcessInfo objInfo = map.get(objl.id);
							objInfo.SetValues(objl, AlertMs);

						} else {
							LogProcessInfo objInfo = new LogProcessInfo();
							objInfo.SetValues(objl, AlertMs);
							map.put(objInfo.EventId, objInfo);
						}
						// for processing large file, batch save would help high memory usage.
						if (map.size() % BatchSize == 0) {
							ProcessDataToDb();
						}

					} catch (Exception ex1) {
						ex1.printStackTrace();
					}
				}
			});
		}

		ProcessDataToDb(); // to store remaining records from map

	}

	/*
	 * Iterates through the HashMap, if record has all info, persist the record in
	 * DB and remove the object from the HashMap.
	 */
	void ProcessDataToDb() throws ClassNotFoundException, SQLException {

		iDBManager mgr = DBManager.GetManager();
		int beginSize = map.size();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			LogProcessInfo info = map.get(key);
			if (info.IsProcessed()) {
				mgr.ExecuteSQL(info.GetInsertRecord());
				it.remove();
			}
		}
		logger.info("No of records persisted: " + (beginSize - map.size()));
	}

	
		// TODO Auto-generated method stub
		
	
}
