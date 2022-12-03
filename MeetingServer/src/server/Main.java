package server;

import javax.swing.UIManager;

import model.User;

public class Main {

	public static SocketController socketController;
	public static MainScreen mainScreen;
	
	public static void main(String[] args, User user, String nameRoom, String portRoom) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainScreen = new MainScreen(user, nameRoom, portRoom);
	
	}
}
