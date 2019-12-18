package mesh;

import fi.utu.tech.distributed.gorilla.logic.Player;

public class PlayerUpdate extends ViestiLuokka {
	public String name;
	public String newName;
	public double angle;
	public double velocity;
	public boolean alive;
	public PlayerUpdate(String name,double angle,double velocity,boolean alive ) {
		super();
		this.name = name;
		this.angle = angle;
		this.velocity = velocity;
		this.alive = alive;
		
	}
	public PlayerUpdate(String name,String newName) {
		super();
		this.name = name;
		this.name = newName;
	}
}
