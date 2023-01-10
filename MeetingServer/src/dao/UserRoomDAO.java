package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.UserRoom;
import server.SocketController;

public class UserRoomDAO {
	private String jdbcURL = "jdbc:mysql://sql6.freesqldatabase.com:3306/sql6589571?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
	private String jdbcUsername = "sql6589571";
	private String jdbcPassword = "dXiafAiM7S";

	private static final String INSERT= "INSERT INTO user_room" + " (id_user, id_room) VALUES" + " (?, ?);";
	private static final String SELECT = "select * from user_room where id = ?;";
	private static final String SELECT_ALL = "select * from user_room;";
	private static final String DELETE = "delete from user_room where id_room = ?;";
	private static final String DELETE_ALL = "delete from user_room;";
	private static final String UPDATE = "update user_room set id_user = ?, id_room = ? where id = ?;";
	
	public UserRoomDAO() {
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

	public void insert(UserRoom userRoom) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			preparedStatement.setInt(1, userRoom.getId_user());
			preparedStatement.setInt(2, userRoom.getId_room());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public UserRoom select(int id) {
		UserRoom userRoom = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id_user = rs.getInt("id_user");
				int id_room = rs.getInt("id_room");
				userRoom = new UserRoom(id, id_user, id_room);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return userRoom;
	}

	public List<UserRoom> selectAll() {

		List<UserRoom> userRoom = new ArrayList<>();
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);) {
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				int id_user = rs.getInt("id_user");
				int id_room = rs.getInt("id_room");
				userRoom.add(new UserRoom(id, id_user, id_room));
			}
			
		} catch (SQLException e) {
			printSQLException(e);
		}
		return userRoom;
	}

	public boolean delete(int idRoom) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE);) {
			statement.setInt(1, idRoom);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	public boolean deleteAll() throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_ALL);) {
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean update(UserRoom userRoom) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE);) {
			statement.setString(1, SocketController.getThisIP());
			statement.setString(1, SocketController.getThisIP());
			statement.setInt(2, userRoom.getId());
			System.out.println(statement);
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
