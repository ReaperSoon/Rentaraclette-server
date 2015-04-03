package fr.stevecohen.dao.old;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.stevecohen.database.ORM;
import fr.stevecohen.database.PrepareStatementCallback;
import fr.stevecohen.dto.UserDto;

public class UserDao extends AbstractDao {
	
	private static UserDao 	instance = null;
	/* Constants */
	private static final String	table = "users";
	
	private UserDao() {
	}

	public static UserDao getInstance() {
		if (instance == null)
			instance = new UserDao();
		return instance;
	}

	public UserDto create(final UserDto userDto, final String password, final String salt) throws SQLException {
		/*
		 * INSERT INTO  `users` (`pseudo` ,  `password` ) VALUES ('myPseydo', MD5('salt' +  'myPass' )
		 */
		int id = new ORM().insert_into(table, "pseudo", "password", "salt").values("?", "?", "?").execUpdate(new PrepareStatementCallback() {
			@Override
			public void prepareStatement(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, userDto.getPseudo());
				preparedStatement.setString(2, password);
				preparedStatement.setString(3, salt);
			}
		});
		userDto.setId(id);
		return userDto;
	}
}
