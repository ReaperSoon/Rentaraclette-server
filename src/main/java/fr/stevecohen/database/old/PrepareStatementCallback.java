package fr.stevecohen.database.old;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementCallback {
	void prepareStatement(PreparedStatement preparedStatement) throws SQLException;
}
