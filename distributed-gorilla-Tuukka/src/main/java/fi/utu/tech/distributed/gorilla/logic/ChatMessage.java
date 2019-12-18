package fi.utu.tech.distributed.gorilla.logic;
import mesh.ViestiLuokka;
/**
 * TODO: make compatible with network play
 */
public final class ChatMessage extends ViestiLuokka{
    public final String sender;
    //public final String recipient;
    public final String contents;

    public ChatMessage(String sender,String contents) {
    	super();
        this.contents = contents;
        this.sender = sender;
    }
    /*public ChatMessage(String sender, String recipient, String contents) {
    	super();
        this.sender = sender;
        this.recipient = recipient;
        this.contents = contents;
    }*/
    
}
