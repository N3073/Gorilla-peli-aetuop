package mesh;
import java.util.List;



import fi.utu.tech.distributed.gorilla.logic.GameConfiguration;

import fi.utu.tech.distributed.gorilla.logic.Player;
public class GameStateUpdate extends ViestiLuokka{
	private GameConfiguration conf;
	private List<Player> remotePlayers;
	public GameStateUpdate(GameConfiguration conf,List<Player> remotePlayers) {
		super();
		this.conf = conf;
		this.remotePlayers = remotePlayers;
	}
	public GameConfiguration getConf() {
		return this.conf;
	}
	public List<Player> getRemotePlayers(){
		return this.remotePlayers;
	}
}
