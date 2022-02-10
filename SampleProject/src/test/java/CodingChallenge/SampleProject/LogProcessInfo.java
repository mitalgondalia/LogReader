package CodingChallenge.SampleProject;

public class LogProcessInfo {
	public String EventId;
	public long EventDuration;
	public boolean Alert;
	public String Type;
	public String Host;
	public long StartTime;
	public long EndTime;

	public boolean IsProcessed() {
		return (StartTime != 0 && EndTime != 0);
	}

	// This will work with all Dbs which supports standard SQL. If we choose to use
	// some different mechanism, will have to overload this.
	public String GetInsertRecord() {

		return "INSERT INTO logInfo (EventId,Duration,alert,Host,type) values ('" + EventId + "'," + EventDuration + ","
				+ Alert + ",'" + Host + "','" + Type + "')";

	}

	/*
	 * This contains logic to set values for the class. 
	 * it would calculate duration and set alert values. Ideally this method should be called twice for each eventId
	 * 
	 */
	public void SetValues(LogInfo info, int AlertMs) {
		if (EventId != null) {
			if (StartTime != 0) // assumed that timestamp value will never be 0 in file
				EndTime = info.timestamp;
			else
				StartTime = info.timestamp;
			EventDuration = EndTime - StartTime;
			if (EventDuration >= AlertMs) {
				Alert = true;

			}
		} else {
			EventId = info.id;
			if (info.state.equals("STARTED")) {
				StartTime = info.timestamp;
			} else {
				EndTime = info.timestamp;
			}
			
		}
		if (Host == null && info.host != null)
			Host = info.host;
		if (Type == null && info.type != null)
			Type = info.type;
	}
}
