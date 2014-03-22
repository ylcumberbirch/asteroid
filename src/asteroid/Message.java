package asteroid;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 2968860633230233137L;
	private int clientId;
	private ArrayList<GameObject> asteroids;
	
	public Message(int clientId, ArrayList<GameObject> asteroids){
		this.clientId = clientId;
		this.asteroids = asteroids;
	}
	
	public void setCliendId(int clientId){
		this.clientId = clientId;
	}
	
	public void setAsteroids(ArrayList<GameObject> asteroids){
		this.asteroids = asteroids;
	}
	
	public int getClientId(){
		return this.clientId;
	}
	
	public ArrayList<GameObject> getAsteroids(){
		return this.asteroids;
	}
}
