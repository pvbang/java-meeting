package model;

public class User {
	protected int id;
	protected String name;
	protected String user_name;
	protected String password;
	
	public User() {
		super();
	}

	public User(String user_name, String password) {
		super();
		this.user_name = user_name;
		this.password = password;
	}

	public User(String name, String user_name, String password) {
		super();
		this.name = name;
		this.user_name = user_name;
		this.password = password;
	}
	
	public User(int id, String name, String user_name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.user_name = user_name;
		this.password = password;
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
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
