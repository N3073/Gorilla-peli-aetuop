package mesh;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class ViestiLuokka implements Serializable {
	
	private final Long token;

	public ViestiLuokka() {
		this.token = generateToken();
	}
	
	// @.return Palauttaa ViestiLuokan instanssin tunnisteen
	public Long getToken() {
		return token;
	}
	/* @.return Random Long-tyyppisen numeron
	 * Käytetään ViestiLuokan konstruktorissa generoimaan viestille uniikki tunniste
	 * Tunnisteen avulla, kun viestejä lähetetään Flooding tekniikalla viesti ei jää kaikumaan ja tukkimaan 
	 * mesh-verkkoa
	 * */
	private Long generateToken() {
		Random rand = new Random();
		Long token = rand.nextLong();
		return token;
	}
	
	/*
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		 
	}
	 
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
			 
	}
	*/
}
