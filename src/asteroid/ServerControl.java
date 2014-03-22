package asteroid;

import java.util.Enumeration;
import java.util.Hashtable;

public class ServerControl extends Thread{
	private Game serverWorld;
	private Hashtable<Integer, Long> clients;
	
	public ServerControl(Game serverWorld, Hashtable<Integer, Long> clients){
		this.serverWorld = serverWorld;
		this.clients = clients;
	}
	
	public Game getServerWorld(){
		return this.serverWorld;
	}
	
	public Hashtable<Integer, Long> getClients(){
		return this.clients;
	}
	
	public void run() {
		long now;
		long lastTime;
		while(this.getServerWorld().isActive()){
			try{
				Enumeration<Integer> valuesEnumeration = this.getClients().keys();
			    while(valuesEnumeration.hasMoreElements()){
			    	int hashKey = valuesEnumeration.nextElement();
			    	lastTime = this.getClients().get(hashKey);
			    	now = System.nanoTime();
			    	if(now > lastTime + 20000000){
			    		this.getServerWorld().refreshClient(hashKey);
			    		this.getClients().remove(hashKey);
			    	}
			    }
			    try {
					Thread.sleep(20);
				} catch (InterruptedException ie) {
				}
			}catch (NullPointerException npe){
				npe.printStackTrace();
			}
		}
	}
}