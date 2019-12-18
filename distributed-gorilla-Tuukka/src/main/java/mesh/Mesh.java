package mesh;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
//import java.util.Scanner;
import java.util.Set;
//import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fi.utu.tech.distributed.gorilla.logic.ChatMessage;
import fi.utu.tech.distributed.gorilla.logic.GameMode;
import fi.utu.tech.distributed.gorilla.logic.GameState;
import fi.utu.tech.distributed.gorilla.logic.GorillaLogic;

public class Mesh extends Thread{
    /**
     * Luo Mesh-palvelininstanssi
     * @param port Portti, jossa uusien vertaisten liittymispyyntöjä kuunnellaan
     */
	private Set<Handler> names = new HashSet<>();
	private final int port;
	private  Set<Long> tokens = new HashSet<>();
	ExecutorService pool = Executors.newFixedThreadPool(500);
	private GorillaLogic logic;
    public Mesh(int port, GorillaLogic logic) {
    	this.logic = logic;
    	this.port = port;
    	 
	}
    public int size() {
    	return names.size();
    }
   
    /**
     * Lähetä hyötykuorma kaikille vastaanottajille
     * @param o Lähetettävä hyötykuorma
     */
    public void broadcast(ViestiLuokka o) {
    	tokenExists(o.getToken());
    	for (Handler saie : names) {
    		//System.out.println(saie.name);
    		try {
    			saie.getOutput().writeObject(o);
    			saie.getOutput().flush();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}

    /**
     * Lähetä hyötykuorma valitulle vertaiselle
     * @param o Lähetettävä hyötykuorma
     * @param recipient Vastaanottavan vertaisen tunnus
     */
    public void send(Serializable o, String recipient) {
	}

    /**
     * Sulje mesh-palvelin ja kaikki sen yhteydet 
     */
    public void close() {
    	for (Handler saie : names) {
    		try {
    			saie.interrupt();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	pool.shutdownNow();
    	this.interrupt();
	}

    /**
     * Lisää token, eli "viestitunniste"
     * Käytännössä merkkaa viestin tällä tunnisteella luetuksi
     * Määreenä private, koska tätä käyttävä luokka on sisäluokka (inner class)
     * Jos et käytä sisäluokkaa, pitää olla public
     * @param token Viestitunniste 
     */
    /*private void addToken(Long token) {
    	tokens.add(token);
    	
	}*/

    /**
     * Tarkista, onko viestitunniste jo olemassa
     * Määreenä private, koska tätä käyttävä luokka on sisäluokka (inner class)
     * Jos et käytä sisäluokkaa, pitää olla public
     * @param token Viestitunniste 
     */
    private boolean tokenExists(Long token) {
        return !tokens.add(token); // HashSet.add() palauttaa false jos token on jo kyseisessä kokoelmassa.  
	}  // nyt palautetaan true, jos token on jo nähty ennen.

    /**
     * Yhdistä tämä vertainen olemassaolevaan Mesh-verkkoon
     * @param addr Solmun ip-osoite, johon yhdistetään
     * @param port Portti, jota vastapuolinen solmu kuuntelee
     */
    public void connect(InetAddress addr, int port) {
    	System.out.println("liitytään osoitteeseen " + addr);
    	try {
                Socket socket = new Socket(addr, port);
    			
    			pool.execute(new Handler(socket));
    			
        
    		
    	} catch (Exception e) {
    		System.out.println(e);
        }
	}

	public void run() {
		
		System.out.println("Starting the server..");
        
        try (var listener = new ServerSocket(port)) {
            System.out.println("Listening to port " + port + " at " + listener.getInetAddress());
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        } catch (Exception e) {
        	e.printStackTrace();
		}
	}
	 /**
     * The client handler task.
     */

    private class Handler extends Thread {
        private String name = new Random().nextInt() + "" + new Random().nextInt();
        private final Socket socket;

        private ObjectOutputStream oOut;
        private ObjectInputStream oIn;

        public Handler(final Socket socket) throws Exception {
          	this.socket = socket;

        }
        
        //
        public ObjectOutputStream getOutput() {
			return oOut;
        }
  
    /**
     *  Käynnistä uusien vertaisten kuuntelusäie
     */
        public void run() {
        	names.add(this);
        	try {InputStream iS = socket.getInputStream();
                    OutputStream oS = socket.getOutputStream();
        			this.oOut = new ObjectOutputStream(oS);
        			this.oIn= new ObjectInputStream(iS);
        			
        		// tarkastetaan onko viesti uusi ja merkitään muistiin
        		while(true) {
        			ViestiLuokka p = (ViestiLuokka) oIn.readObject();
        			if(!tokenExists(p.getToken())) {

        				if(p instanceof ChatMessage) {

        					ChatMessage message = (ChatMessage) p;

        					System.out.println(message.sender +": " + message.contents + p.getToken());
        				}else if(p instanceof PlayerUpdate) {
        					logic.updateGameState((PlayerUpdate) p);
        				}else if(p instanceof GameState) {
        					logic.loadGameState((GameState) p);
        					logic.setMode(GameMode.Game);
        					logic.views.setGameState((GameState)p);
        				}
        				
        				broadcast(p);
        			
        				}
        			}	
        		

        		} catch (Exception e) {
        			e.printStackTrace();
        		}


        		
        }
    }
}
