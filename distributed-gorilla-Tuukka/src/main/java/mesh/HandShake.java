package mesh;

import java.io.Serializable;

public class HandShake extends ViestiLuokka implements Serializable{
	private int validation;
	protected void setValidation() {
		this.validation = 000000000000;
	}
	protected int getValidation() {
		return validation;
	}
}
