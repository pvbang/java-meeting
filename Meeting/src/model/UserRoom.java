package model;

public class UserRoom {
	protected int id;
	protected int id_user;
	protected int id_room;
	
	public UserRoom() {
		super();
	}

	public UserRoom(int id_user, int id_room) {
		super();
		this.id_user = id_user;
		this.id_room = id_room;
	}

	public UserRoom(int id, int id_user, int id_room) {
		super();
		this.id = id;
		this.id_user = id_user;
		this.id_room = id_room;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public int getId_room() {
		return id_room;
	}
	public void setId_room(int id_room) {
		this.id_room = id_room;
	}
	
	
}
