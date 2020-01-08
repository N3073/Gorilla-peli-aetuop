package mesh;

import java.util.ArrayList;

public class Ping extends ViestiLuokka{
	public ArrayList<String> contacts;
	public final boolean echo;
	public final String senderId;
	public Ping(String senderId) {
		super();
		this.senderId = senderId;
		echo = false;
	}
	public Ping(Ping ping) {
		super();
		this.senderId = ping.senderId;
		this.contacts=ping.contacts;
		echo = true;
	}
}
