package CodingChallenge.SampleProject;

import java.sql.Connection;
import java.sql.SQLException;

public interface iDBManager {
Connection GetDBConn() throws ClassNotFoundException, SQLException;
void ExecuteSQL(String sql) throws SQLException, ClassNotFoundException;
//we could add common db operations here like GetResultSet , GetScalerResult, etc
}
