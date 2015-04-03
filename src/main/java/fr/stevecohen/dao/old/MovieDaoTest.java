package fr.stevecohen.dao.old;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.JSONException;

import fr.stevecohen.database.ORM;
import fr.stevecohen.database.PrepareStatementCallback;
import fr.stevecohen.dto.MovieDto;

public class MovieDaoTest extends AbstractDao {
	
	private static MovieDaoTest 	instance = null;
	/* Constants */
	private static final String	table = "movies";
	
	private MovieDaoTest() {
	}

	public static MovieDaoTest getInstance() {
		if (instance == null)
			instance = new MovieDaoTest();
		return instance;
	}

	public MovieDto create(final MovieDto movieDto) throws SQLException {
		/* INSERT INTO `movies`
		* (`id`, `title`, `description`, `style`, `time`, `downloaded`) VALUES
		* (NULL, 'TITRE', 'DESCRIPTION', 'STYLE', '999', '1');
		*/
		int id = new ORM()
		.insert_into(table, "title", "description", "style", "time", "downloaded")
		.values("?", "?", "?", "?", "?")
		.execUpdate(new PrepareStatementCallback() {
			@Override
			public void prepareStatement(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, movieDto.getTitle());
				preparedStatement.setString(2, movieDto.getDescription());
				preparedStatement.setString(3, movieDto.getStyle());
				preparedStatement.setString(4, movieDto.getTime());
				preparedStatement.setBoolean(5, movieDto.isDownloaded());
			}
		});
		movieDto.setId(id);
		return movieDto;
	}
	
	public MovieDto update(final MovieDto movieDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException, SQLException {
		/*
		 * UPDATE `movies` 
		 * SET `title` = 'aa1', `description` = 'bb1', `style` = 'cc1', 
		 * `time` = 'dd1', `downloaded` = '0' WHERE `movies`.`id` = 1;
		 */
		new ORM()
		.update(table).set(movieDto.toMap(), true).where("id", "=", String.valueOf(movieDto.getId())).execUpdate(new PrepareStatementCallback() {
			@Override
			public void prepareStatement(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, movieDto.getId());
				preparedStatement.setString(2, movieDto.getTitle());
				preparedStatement.setString(3, movieDto.getDescription());
				preparedStatement.setString(4, movieDto.getStyle());
				preparedStatement.setString(5, movieDto.getTime());
				preparedStatement.setBoolean(6, movieDto.isDownloaded());
				preparedStatement.setBoolean(7, movieDto.isDeleted());
			}
		});
		return movieDto;
	}
	
	public List<MovieDto> selectAll() throws SQLException, JSONException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		/*
		 * SELECT * FROM `movies`;
		 */
		ResultSet result = new ORM()
		.select("*").from(table).where("deleted", "!=", "1").execQuery(null);
		List<HashMap<String, Object>> rawMovies = databaseUtil.getResultFrom(result);
		List<MovieDto> movies = new ArrayList<MovieDto>(rawMovies.size());
		for (int i = rawMovies.size() - 1; i >= 0; i--) {
			movies.add((MovieDto) new MovieDto().fromMap(rawMovies.get(i)));
		}
		return movies;
	}
	
	public List<MovieDto> selectAllInTrash() throws SQLException, JSONException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		/*
		 * SELECT * FROM `movies`;
		 */
		ResultSet result = new ORM()
		.select("*").from(table).where("deleted", "=", "1").execQuery(null);
		List<HashMap<String, Object>> rawMovies = databaseUtil.getResultFrom(result);
		List<MovieDto> movies = new ArrayList<MovieDto>(rawMovies.size());
		for (int i = rawMovies.size() - 1; i >= 0; i--) {
			movies.add((MovieDto) new MovieDto().fromMap(rawMovies.get(i)));
		}
		return movies;
	}
	
	public List<MovieDto> selectByIds(String[] ids) throws SQLException, JSONException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		/*
		 * SELECT * FROM `movies` WHERE `movies`.`id` in (1, 2, 3, 4, 5);
		 */
		ResultSet result = new ORM()
		.select("*").from(table).where("id", ids).execQuery(null);
		List<HashMap<String, Object>> rawMovies = databaseUtil.getResultFrom(result);
		List<MovieDto> movies = new ArrayList<MovieDto>(rawMovies.size());
		for (int i = rawMovies.size() - 1; i >= 0; i--) {
			movies.add((MovieDto) new MovieDto().fromMap(rawMovies.get(i)));
		}
		return movies;
	}
	
	public MovieDto selectById(String id) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, SQLException, JSONException {
		/*
		 * SELECT * FROM `movies` WHERE `movies`.`id` = '1';
		 */
		ResultSet result = new ORM()
		.select("*").from(table).where("id", "=", id).execQuery(null);
		List<HashMap<String, Object>> rawMovies = databaseUtil.getResultFrom(result);
		if (!rawMovies.isEmpty()) {
			return (MovieDto) (new MovieDto().fromMap(rawMovies.get(0)));
		}
		return null;
	}
	
	public void deleteById(String id) throws SQLException {
		/*
		 * UPDATE  `towatchlist_dev`.`movies` SET  `deleted` =  '1' WHERE  `movies`.`id` =5;
		 */
		new ORM()
		.update(table).set("deleted", "1").where("id", "=", id).execUpdate(null);
	}
}
