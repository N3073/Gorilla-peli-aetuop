package mesh;

import java.util.ArrayList;

public class Ping extends ViestiLuokka{
	public ArrayList<String> contacts;
	public final boolean echo;
	public final String senderId;
	public Ping(String senderId) {
		super();
		this.contacts= new ArrayList<String>();
		this.senderId = senderId;
		this.echo = false;
	}
	public Ping(Ping ping) {
		super();
		this.senderId = ping.senderId;
		this.contacts=ping.contacts;
		this.echo = true;
	}
}
