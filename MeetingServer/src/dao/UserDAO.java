package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://sql6.freesqldatabase.com:3306/sql6581708?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
	private String jdbcUsername = "sql6581708";
	private String jdbcPassword = "lfvceV4iVW";

	private static final String INSERT_USER = "INSERT INTO users" + " (name, user_name, password) VALUES" + " (?, ?, ?);";
	private static final String SELECT_USER = "select id, name, user_name, password from users where id = ?;";
	private static final String SELECT_ALL_USERS = "select * from users;";
	private static final String DELETE_USER = "delete from users where id = ?;";
	private static final String DELETE_ALL_USERS = "delete from users;";
	private static final String UPDATE_USER = "update users set name = ?, user_name = ?, password = ? where id = ?;";
	
	public UserDAO() {
		
	}
	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public void insertUser(User user) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getUser_name());
			preparedStatement.setString(3, user.getPassword());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User selectUser(int id) {
		User user = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String user_name = rs.getString("user_name");
				String password = rs.getString("password");
				user = new User(id, name, user_name, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public List<User> selectAllUsers() {

		List<User> users = new ArrayList<>();
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String user_name = rs.getString("user_name");
				String password = rs.getString("password");
				users.add(new User(id, name, user_name, password));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USER);) {
			statement.setInt(1, id);
			System.out.println(statement);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	public boolean deleteAllUser() throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_ALL_USERS);) {
			System.out.println(statement);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USER);) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getUser_name());
			statement.setString(3, user.getPassword());
			statement.setInt(4, user.getId());
			System.out.println(statement);
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	
}
