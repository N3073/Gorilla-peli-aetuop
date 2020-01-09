package mesh;

import fi.utu.tech.distributed.gorilla.logic.MoveThrowBanana;
import fi.utu.tech.distributed.gorilla.logic.Player;

public class PlayerUpdate extends ViestiLuokka {
	public String name;
	public MoveThrowBanana mtb;
	
	public PlayerUpdate(String name, MoveThrowBanana mtb ) {
		super();
		this.name = name;
		this.mtb = mtb;
		
	}
	
}
