/**
 * 
 */
package asteroid;
/**
 * @author yazbod
 *
 */

import static org.lwjgl.opengl.GL11.*;

import tools.Vector;
import tools.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
public class Game implements Serializable{

	private static final long serialVersionUID = -7813492273200335324L;
	/**
	 * @param args
	 */
	private boolean active;
	private Vector oglCameraPosition;
	private Vector oglCameraFoa;
	//private int width;
	//private int height;
	private ArrayList<GameObject> objects;
	
	public Game(int numObjects, int clientID)
	{
		this.active = true;
		this.oglCameraPosition = new Vector (0,0,100);
		this.oglCameraFoa = new Vector (0,0,0);
		this.objects = new ArrayList <GameObject>();
		Vector color = new Vector(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat());
		for (int i = 0; i < numObjects;i++){
			Asteroid asteroid = new Asteroid(Constants.DEFAULT_SIZE, clientID);
			asteroid.setColor(color);
			this.objects.add(asteroid);
		}
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public boolean isActive()
	{
		return this.active;
	}
	
	public Vector getCameraFoa(){
		return this.oglCameraFoa;
	}
	
	public Vector getCameraPosition(){
		return this.oglCameraPosition;
	}
	
	public void setCameraFoa(Vector foa){
		this.oglCameraFoa = foa;
	}
	
	public void setCameraPosition(Vector position){
		this.oglCameraPosition = position;
	}
	
	public synchronized ArrayList<GameObject> getObjects(){
		return this.objects;
	}
	
	public synchronized void refreshClient(int clientId){
		Iterator<GameObject> iter = this.getObjects().iterator();
		while (iter.hasNext()) {
		    if (iter.next().getClientID() == clientId) {
		        iter.remove();
		    }
		}
	}
	
	public synchronized void addObject(GameObject object){
		this.getObjects().add(object);
	}
	
	public synchronized void draw(){
		glLoadIdentity();
		for (int i = 0; i < this.objects.size(); i++)
		{
			objects.get(i).draw();
		}
		glFlush();
	}
	
	public synchronized void update(long time){
		for (int i = 0; i < this.getObjects().size(); i++)
		{
			long now = System.nanoTime();
			long t = (((long)(now - time)/1000000)); // t in seconds
			this.getObjects().get(i).updatePosition(t);
		}
	}
}