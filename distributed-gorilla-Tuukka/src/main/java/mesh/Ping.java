package mesh;

import java.util.ArrayList;

public class Ping extends ViestiLuokka{
	public final String senderId;
	public boolean echo;
	public final String replyId;
	public String replyName;
	public Ping(String senderId) {
		super();
		this.echo=false;
		this.senderId = senderId;
		this.replyId="";
	}
	public Ping(Ping ping, String replyId, String replyName) {
		super();
		this.echo=true;
		this.senderId = ping.senderId;
		this.replyId = replyId;
		this.replyName=replyName;
	}
}
