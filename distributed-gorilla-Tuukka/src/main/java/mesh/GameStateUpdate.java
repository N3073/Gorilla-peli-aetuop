package mesh;
import java.util.List;



import fi.utu.tech.distributed.gorilla.logic.GameConfiguration;

import fi.utu.tech.distributed.gorilla.logic.Player;
public class GameStateUpdate extends ViestiLuokka{
	private List<String> names;
	private List<Player> remotePlayers;
	public GameStateUpdate(List<String> names,List<Player> remotePlayers) {
		super();
		this.names = names;
		this.remotePlayers = remotePlayers;
	}
	public List<String> getNames() {
		return this.names;
	}
	public List<Player> getRemotePlayers(){
		return this.remotePlayers;
	}
	public void removeSelf(String player) {
		remotePlayers.remove(player);
	}
}
