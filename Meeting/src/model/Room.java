package model;

public class Room {
	protected int id;
	protected String name;
	protected String ip;
	protected String port;
	protected String code_room;
	protected int id_user;
	
	public Room() {
		super();
	}

	public Room(String name, String ip, String port, String code_room, int id_user) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.code_room = code_room;
		this.id_user = id_user;
	}

	public Room(int id, String name, String ip, String port, String code_room, int id_user) {
		super();
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.code_room = code_room;
		this.id_user = id_user;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getCode_room() {
		return code_room;
	}
	public void setCode_room(String code_room) {
		this.code_room = code_room;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	
	
}
