package mesh;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
//import java.util.Scanner;
import java.util.Set;
//import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import fi.utu.tech.distributed.gorilla.logic.ChatMessage;
import fi.utu.tech.distributed.gorilla.logic.GameMode;
import fi.utu.tech.distributed.gorilla.logic.GameState;
import fi.utu.tech.distributed.gorilla.logic.GorillaLogic;

public class Mesh extends Thread{
    /**
     * Luo Mesh-palvelininstanssi
     * @param port Portti, jossa uusien vertaisten liittymispyyntöjä kuunnellaan
     */
	private final String id;
	private Set<Handler> names = new HashSet<>();
	private final int port;
	private  Set<Long> tokens1 = new HashSet<>();
	private  Set<Long> tokens2 = new HashSet<>();
	private ArrayList<String> contacts = new ArrayList<String>();
	private ExecutorService pool = Executors.newFixedThreadPool(500);
	private LinkedBlockingQueue<ViestiLuokka> logicInputs = new LinkedBlockingQueue<ViestiLuokka>();

	
	
    public Mesh(int port) {
    	this.id = new Random().nextInt()+"";
    	this.port = port;
    	 
	}
    public LinkedBlockingQueue<ViestiLuokka> getLogicInputs(){
    	return this.logicInputs;
    }
    public int size() {
    	return names.size();
    }
    public String getID() {
    	return this.id;
    }
    public ArrayList<String> getContacts(){
    	return this.contacts;
    }
    /*public Set<Handler> getNames(){
    	return this.names;
    }
   
    /**
     * Lähetä hyötykuorma kaikille vastaanottajille
     * @param o Lähetettävä hyötykuorma
     */
    public synchronized void broadcast(ViestiLuokka o) {
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
    	broadcast(new ResignationLetter());
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
     * Tarkista, onko viestitunniste jo olemassa
     * Määreenä private, koska tätä käyttävä luokka on sisäluokka (inner class)
     * Jos et käytä sisäluokkaa, pitää olla public
     * Käytetään kahta hashsettiä, jotta voidaan kontrolloida tallennettujen tokenien määrää.
     * @param token Viestitunniste 
     */
    private synchronized boolean tokenExists(Long token) {
    	int vuoro = 1;
    	int sallittuKoko = 40;
    	/*
    	 * jos token on jo olemassa sitä ei tarvitse lisätä ja palautetaan true
    	 * 
    	 * */
    	if(tokens1.contains(token) | tokens2.contains(token)) {
    		return true;
    	}
    	/*
    	 * Vältetään työmuistin täyttyminen poistamalla vanhoja tokeneita.
    	 * Jos tokens1 tai tokens2 settien koot ylittävät sallitun koon.
    	 * tyhjennetään vanhempi puolisko ja sitä aletaan täyttää uudestaan.
    	 * 
    	 * */
    	if(vuoro == 1) {
    		tokens1.add(token);
    		if(tokens1.size()>sallittuKoko/2) {
    			vuoro = 2;
    			tokens2.clear();
    		}
    	}else if(vuoro == 2) {
    		tokens2.add(token);
    		if(tokens2.size()>sallittuKoko/2) {
    			vuoro = 1;
    			tokens1.clear();
    		}
    	}
    	return false;
	}

    /**
     * Yhdistä tämä vertainen olemassaolevaan Mesh-verkkoon
     * @param addr Solmun ip-osoite, johon yhdistetään
     * @param port Portti, jota vastapuolinen solmu kuuntelee
     */
    public void connect(InetAddress addr, int port) {
    	System.out.println("liitytään osoitteeseen " + addr);
    	try {
                Socket socket = new Socket(addr, port);
    			pool.execute(new Handler(socket,true));
    			
        
    		
    	} catch (Exception e) {
    		System.out.println(e.getLocalizedMessage());
        }
	}

	public void run() {
		
	//	System.out.println("Starting the server..");
        
        try (var listener = new ServerSocket(port)) {
           
            while (true) {
                pool.execute(new Handler(listener.accept(),false));
            }
        } catch (Exception e) {
        	//System.out.println(e);
		}
	}
	 /**
     * The client handler task.
     */

    private class Handler extends Thread {
        private String name = new Random().nextInt() + "" + new Random().nextInt();
        private final Socket socket;
        private boolean og;

        private ObjectOutputStream oOut;
        private ObjectInputStream oIn;

        public Handler(final Socket socket, boolean og) throws Exception {
          	this.socket = socket;
          	this.og = og;
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
        		if(og) {
        			HandShake firstContact = new HandShake();
        			oOut.writeObject(firstContact);
        			oOut.flush();
					Thread.sleep(10);
        			ViestiLuokka rebound = (ViestiLuokka) oIn.readObject();
        			if(rebound instanceof HandShake) {
        				
        				if(((HandShake)rebound).getValidation() != 000000000000) {
        					names.remove(this);
        					socket.close();
            				interrupt();
        				}
        			}else {
        				names.remove(this);
        				socket.close();
        				interrupt();
        			}
        		}
        		System.out.println("Yhdistetty koneeseen");
        		this.og=false;
        		// tarkastetaan onko viesti uusi ja merkitään muistiin
        		while(true) {
        			
        			ViestiLuokka p = (ViestiLuokka) oIn.readObject();
        			if(!tokenExists(p.getToken())) {
        				/*if(p instanceof ChatMessage) {
        					broadcast(p);
        					ChatMessage message = (ChatMessage) p;
        					System.out.println(message.sender +": " + message.contents);
        					
        					
        					
        					
        					
        				}else if(p instanceof PlayerUpdate) {
        					broadcast(p);
        					PlayerUpdate pu = (PlayerUpdate)p;
        					logic.handleThrowBanana(pu.mtb,pu.name);
        					
        				}else if(p instanceof GameStateUpdate) {
        					broadcast(p);
        					System.out.println("konfiguraatio läpi");
        					
        					GameStateUpdate gsu = (GameStateUpdate) p;
        					logic.setOtherPlayers(gsu.remotePlayers);
        					newGameState=gsu;
        					newgame = true;
        					
        					
        					
        				
        					
        					
        					
        					
        				}*/
        				if(p instanceof HandShake){
        					HandShake rebound = (HandShake)p;
        					rebound.setValidation();
        					oOut.writeObject(rebound);
                			oOut.flush();
        					
        					
        				}else if(p instanceof ResignationLetter) {
        					System.out.println("connection lost");
        					socket.close();
        					names.remove(this);
            				interrupt();
            				break;
        					
        					
        					
        					
        					
        					
        					
        					
        				}else if(p instanceof Ping){
            				Ping ping = (Ping)p;
            				if(ping.senderId.equals(id) && ping.echo) {
            					contacts.add(((Ping)p).replyId);
            					//System.out.println("vastaaan otettu ping"+contacts.size());
            				}else {
            					broadcast(new Ping((Ping)p,id));
            				}
            			}else {
            				broadcast(p);
            				logicInputs.put(p);
            			}
        				
        				
        			
        				}
        				
        			
        			}

        		} catch (Exception e) {
        			e.printStackTrace();
        		}


        		
        }
    }
}
