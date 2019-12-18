package fi.utu.tech.distributed.gorilla;

import java.net.InetAddress;

import mesh.Mesh;
import mesh.ViestiLuokka;

/**
 * This is the main class. In order to launch JavaFX from an IDE, needs to call a different
 * "Application" class (here App). Note, App.launch will block until the GUI application is closed.
 */
public class Main {
    public static void main(String[] args) {
    	try {
    	App.launch(App.class, args);
    	/*Thread verkko = new Thread(new Mesh(1234));
    	verkko.start();
    	Thread.currentThread().sleep(2000);
    	
    	
    	
    	
    	Mesh liittyjä = new Mesh(1234);
    	InetAddress addr = InetAddress.getByName("127.0.0.1");
    	System.out.println("uusi liittyjä");
    	liittyjä.connect(addr ,1234);
    	Thread.currentThread().sleep(2000);
    	ViestiLuokka viesti = new ViestiLuokka("hei maailma");
    	liittyjä.broadcast(viesti);
    	Thread.currentThread().sleep(10000);
    	*/
    	} catch (Exception e) {
    		System.out.println(e);
        } 
    }
}