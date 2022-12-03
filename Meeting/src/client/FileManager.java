package client;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ServerDAO;
import dao.UserDAO;
import dao.UserRoomDAO;
import model.Server;
import model.User;
import model.UserRoom;

public class FileManager {

//	public final static String SERVER_FILE = "config.txt";
	
	private static UserDAO userDAO = new UserDAO();
	private static ServerDAO serverDAO = new ServerDAO();
	private static UserRoomDAO userRoomDAO = new UserRoomDAO();
	
	public static boolean exist(String fileName) {
		return (new File(fileName)).isFile();
	}

	public static List<ServerData> getServerList(User user) {
		List<ServerData> serverList = new ArrayList<ServerData>();
		
		List<UserRoom> userRoomList = new ArrayList<UserRoom>();
		userRoomList = userRoomDAO.selectAll();
		
		if (userRoomList == null) {
			return serverList;
		}
		
		List<User> userList = new ArrayList<User>();
		userList = userDAO.selectAllUsers();
		
		List<Server> roomList = new ArrayList<Server>();
		roomList = serverDAO.selectAllServers();
		
		User userFind = new User();
		Server serverFind = new Server();
		
		for (UserRoom ur : userRoomList) {
			if (user.getId() == ur.getId_user()) {
				for (Server s : roomList) {
					if (ur.getId_room() == s.getId()) {
						serverFind = s;
						for (User u : userList) {
							if (serverFind.getId_user() == u.getId()) {
								userFind = u;
							}
						}
					}
				}
				
				serverList.add(new ServerData(serverFind.getId(), serverFind.getName(), serverFind.getCode_server(), userFind.getName(), serverFind.getIp(), serverFind.getPort(), false, 0));
			}
		}
		
		return serverList;
	}

	public static void setServerList(Server server) throws SQLException {
		serverDAO.updateServer(server);
	}

	public static Object[][] getServerObjectMatrix(List<ServerData> serverList) {
		if (serverList == null)
			return new Object[][] {};
		Object[][] serverObjMatrix = new Object[serverList.size()][8];
		for (int i = 0; i < serverList.size(); i++) {
			
			serverObjMatrix[i][0] = serverList.get(i).id;
			serverObjMatrix[i][1] = serverList.get(i).name;
			serverObjMatrix[i][2] = serverList.get(i).code_room;
			serverObjMatrix[i][3] = serverList.get(i).name_user;
			serverObjMatrix[i][4] = serverList.get(i).ip;
			serverObjMatrix[i][5] = serverList.get(i).port;
			serverObjMatrix[i][6] = serverList.get(i).isOpen ? "Hoạt động" : "Không hoạt động";
			serverObjMatrix[i][7] = serverList.get(i).connectAccountCount;
		}
		return serverObjMatrix;
	}
}
