package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Server;
import server.SocketController;

public class ServerDAO {
	private String jdbcURL = "jdbc:mysql://sql6.freesqldatabase.com:3306/sql6581708?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
	private String jdbcUsername = "sql6581708";
	private String jdbcPassword = "lfvceV4iVW";

	private static final String INSERT_SERVER = "INSERT INTO rooms" + " (name, ip, port, code_room, id_user) VALUES" + " (?, ?, ?, ?, ?);";
	private static final String SELECT_SERVER = "select * from rooms where id = ?;";
	private static final String SELECT_SERVER_CODE = "select * from rooms where code_room = ?;";
	private static final String SELECT_ALL_SERVERS = "select * from rooms;";
	private static final String DELETE_SERVER = "delete from rooms where id = ?;";
	private static final String DELETE_ALL_SERVERS = "delete from rooms;";
	private static final String UPDATE_SERVER = "update rooms set ip = ? where id = ?;";
	private static final String UPDATE_NAME_SERVER = "update rooms set name = ? where id = ?;";
	
	public ServerDAO() {
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

	public void insertServer(Server server) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SERVER)) {
			preparedStatement.setString(1, server.getName());
			preparedStatement.setString(2, server.getIp());
			preparedStatement.setString(3, server.getPort());
			preparedStatement.setString(4, server.getCode_server());
			preparedStatement.setInt(5, server.getId_user());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Server selectServer(int id) {
		Server server = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SERVER);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String ip = rs.getString("ip");
				String port = rs.getString("port");
				String code_server = rs.getString("code_room");
				int id_user = rs.getInt("id_user");
				server = new Server(id, name, ip, port, code_server, id_user);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return server;
	}
	
	public Server selectServerCode(String code_room) {
		Server server = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SERVER_CODE);) {
			preparedStatement.setString(1, code_room);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String ip = rs.getString("ip");
				String port = rs.getString("port");
				int id_user = rs.getInt("id_user");
				server = new Server(id, name, ip, port, code_room, id_user);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return server;
	}

	public List<Server> selectAllServers() {

		List<Server> servers = new ArrayList<>();
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SERVERS);) {
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String ip = rs.getString("ip");
				String port = rs.getString("port");
				String code_server = rs.getString("code_room");
				int id_user = rs.getInt("id_user");
				servers.add(new Server(id, name, ip, port, code_server, id_user));
			}
			
		} catch (SQLException e) {
			printSQLException(e);
		}
		return servers;
	}

	public boolean deleteServer(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_SERVER);) {
			statement.setInt(1, id);
			System.out.println(statement);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	public boolean deleteAllServer() throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_ALL_SERVERS);) {
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateServer(Server server) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_SERVER);) {
			statement.setString(1, SocketController.getThisIP());
			statement.setInt(2, server.getId());
			System.out.println(statement);
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	public boolean updateNameServer(int id, String name) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_NAME_SERVER);) {
			statement.setString(1, name);
			statement.setInt(2, id);
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
