package client;

public class ServerData {
//	public String nickName;
//	public String code_room;
//	public String realName;
//	public String ip;
//	public int port;
//	public boolean isOpen;
//	public int connectAccountCount;
//	protected String code_server;
//	
	public int id;
	public String name;
	public String code_room;
	public int id_user;
	public String ip;
	public String port;
	public boolean isOpen;
	public int connectAccountCount;
	public String name_user;
	
	public ServerData(String name, String code_room, int id_user, String ip, String port) {
		super();
		this.name = name;
		this.code_room = code_room;
		this.id_user = id_user;
		this.ip = ip;
		this.port = port;
		this.isOpen = false;
		this.connectAccountCount = 0;
	}

	public ServerData(String name, String code_room, int id_user, String ip, String port, boolean isOpen,
			int connectAccountCount) {
		super();
		this.name = name;
		this.code_room = code_room;
		this.id_user = id_user;
		this.ip = ip;
		this.port = port;
		this.isOpen = isOpen;
		this.connectAccountCount = connectAccountCount;
	}
	
	public ServerData(String name, String code_room, String name_user, String ip, String port, boolean isOpen,
			int connectAccountCount) {
		super();
		this.name = name;
		this.code_room = code_room;
		this.name_user = name_user;
		this.ip = ip;
		this.port = port;
		this.isOpen = isOpen;
		this.connectAccountCount = connectAccountCount;
	}
	
	public ServerData(int id, String name, String code_room, String name_user, String ip, String port, boolean isOpen,
			int connectAccountCount) {
		super();
		this.id = id;
		this.name = name;
		this.code_room = code_room;
		this.name_user = name_user;
		this.ip = ip;
		this.port = port;
		this.isOpen = isOpen;
		this.connectAccountCount = connectAccountCount;
	}

	public ServerData(int id, String name, String code_room, int id_user, String ip, String port, boolean isOpen,
			int connectAccountCount) {
		super();
		this.id = id;
		this.name = name;
		this.code_room = code_room;
		this.id_user = id_user;
		this.ip = ip;
		this.port = port;
		this.isOpen = isOpen;
		this.connectAccountCount = connectAccountCount;
	}
	
	
//	public ServerData(String name, String ip, int port) {
//		this.nickName = name;
//		this.realName = "";
//		this.ip = ip;
//		this.port = port;
//		this.isOpen = false;
//		this.connectAccountCount = 0;
//	}
//
//	public ServerData(String name, String ip, int port, String code_server) {
//		this.nickName = name;
//		this.realName = "";
//		this.ip = ip;
//		this.port = port;
//		this.isOpen = false;
//		this.connectAccountCount = 0;
//		this.code_server = code_server;
//	}
//	
//	public ServerData(String name, String code_room, String realName, String ip, int port, String code_server) {
//		this.nickName = name;
//		this.code_room = code_room;
//		this.realName = realName;
//		this.ip = ip;
//		this.port = port;
//		this.isOpen = false;
//		this.connectAccountCount = 0;
//		this.code_server = code_server;
//	}
//	
//	public ServerData(String nickName, String realName, String ip, int port, boolean isOpen, int connectAccountCount) {
//		this.nickName = nickName;
//		this.realName = realName;
//		this.ip = ip;
//		this.port = port;
//		this.isOpen = isOpen;
//		this.connectAccountCount = connectAccountCount;
//	}
//	
//	public ServerData(String nickName, String realName, String ip, int port, boolean isOpen, int connectAccountCount, String code_server) {
//		this.nickName = nickName;
//		this.realName = realName;
//		this.ip = ip;
//		this.port = port;
//		this.isOpen = isOpen;
//		this.connectAccountCount = connectAccountCount;
//		this.code_server = code_server;
//	}
//	
//	public ServerData(String nickName, String code_room, String realName, String ip, int port, boolean isOpen, int connectAccountCount, String code_server) {
//		this.nickName = nickName;
//		this.code_room = code_room;
//		this.realName = realName;
//		this.ip = ip;
//		this.port = port;
//		this.isOpen = isOpen;
//		this.connectAccountCount = connectAccountCount;
//		this.code_server = code_server;
//	}

}