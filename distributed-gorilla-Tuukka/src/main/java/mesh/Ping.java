package mesh;

import java.util.ArrayList;

public class Ping extends ViestiLuokka{
	public final String senderId;
	public boolean echo;
	public final String replyId;
	public Ping(String senderId) {
		super();
		this.echo=false;
		this.senderId = senderId;
		this.replyId="";
	}
	public Ping(Ping ping, String replyId) {
		super();
		this.echo=true;
		this.senderId = ping.senderId;
		this.replyId = replyId;
	}
}
