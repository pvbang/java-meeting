package model;

public class Server {
	protected int id;
	protected String name;
	protected String ip;
	protected String port;
	protected String code_server;
	protected int id_user;
	
	public Server() {
		super();
	}

	public Server(String name, String ip, String port, String code_server, int id_user) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.code_server = code_server;
		this.id_user = id_user;
	}

	public Server(int id, String name, String ip, String port, String code_server, int id_user) {
		super();
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.code_server = code_server;
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
	public String getCode_server() {
		return code_server;
	}
	public void setCode_server(String code_server) {
		this.code_server = code_server;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	
	
}
