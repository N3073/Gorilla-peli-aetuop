package mesh;
import java.util.List;



import fi.utu.tech.distributed.gorilla.logic.GameConfiguration;

import fi.utu.tech.distributed.gorilla.logic.Player;
public class GameStateUpdate extends ViestiLuokka{
	public GameConfiguration conf;
	public List<Player> remotePlayers;
	public GameStateUpdate(GameConfiguration conf,List<Player> remotePlayers) {
		super();
		this.conf = conf;
		this.remotePlayers = remotePlayers;
	}
}
