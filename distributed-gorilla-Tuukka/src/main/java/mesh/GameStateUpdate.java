package mesh;
import fi.utu.tech.distributed.gorilla.logic.GameConfiguration;
import fi.utu.tech.distributed.gorilla.logic.GameState;
public class GameStateUpdate extends ViestiLuokka{
	public GameConfiguration gs;
	public GameStateUpdate(GameConfiguration gs) {
		super();
		this.gs = gs;
	}
}
