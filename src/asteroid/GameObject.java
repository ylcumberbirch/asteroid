package asteroid;

import java.io.Serializable;

import tools.Constants;
import tools.Vector;
import static org.lwjgl.opengl.GL11.*;

public abstract class GameObject implements Drawable, Serializable{
	
	private static final long serialVersionUID = 2016082203350718095L;
	private Vector position;
	private  transient Vector speed;
	private int id;
	private int clientID;
		
	//Init
	public GameObject(int clientID){
		float posX = (float)((2*Math.random()*Constants.LIM_X)-Constants.LIM_X);
		float posY = (float)((2*Math.random()*Constants.LIM_Y)-Constants.LIM_Y);
		float posZ = (float)((2*Math.random()*Constants.LIM_Z)-Constants.LIM_Z);
		//float posZ = 50f;
		float speedX = (float)(((2*Math.random())-1)*Constants.MAX_SPEED);
		float speedY = (float)(((2*Math.random())-1)*Constants.MAX_SPEED);
		float speedZ = (float)(((2*Math.random())-1)*Constants.MAX_SPEED);
		//float speedZ = 0;
		this.position = new Vector(posX, posY, posZ);
		this.speed = new Vector(speedX, speedY, speedZ);
		this.id = (int)(Math.floor(1000000*Math.random()));
		this.clientID = clientID;
	}
	
	public GameObject(Vector position, Vector speed, int clientID){
		this.position = position;
		this.speed = speed;
		this.id = (int)(Math.floor(1000000*Math.random()));
		this.clientID = clientID;
	}
	
	//Setters
	public void setPosition(float x, float y, float z){
		this.position = new Vector (x, y, z);
	}
	
	public void setPosition(Vector pos){
		this.position = new Vector (pos);
	}
	
	public void setSpeed(float x, float y, float z){
		this.speed = new Vector(x,y,z);
	}
	
	public void setSpeed(Vector speed)
	{
		this.speed = new Vector(speed);
	}
	
	public void setClientID(int clientID){
		this.clientID = clientID;
	}
	
	public void setID (int id){
		this.id = id;
	}
	
	//Getters
	public int getClientID(){
		return this.clientID;
	}
	
	
	public int getID(){
		return this.id;
	}
	public Vector getPosition(){
		return this.position;
	}
	
	public Vector getSpeed(){
		return this.speed;
	}
	
	//Methods
	public void updatePosition(long time){
		/*
		 * Updated position with the speed after time given 
		 */
		Vector auxPos = new Vector(this.getPosition());
		Vector auxSpeed = new Vector (this.getSpeed());
		auxSpeed.multiply(time);
		auxPos.add(auxSpeed);
		if (auxPos.getX() > Constants.LIM_X){
			this.getSpeed().setX(-this.getSpeed().getX());
			auxPos.setX(Constants.LIM_X);
		}
		if (auxPos.getX() < -Constants.LIM_X){
			this.getSpeed().setX(-this.getSpeed().getX());
			auxPos.setX(-Constants.LIM_X);
		}
		if (auxPos.getY() > Constants.LIM_Y){
			this.getSpeed().setY(-this.getSpeed().getY());
			auxPos.setY(Constants.LIM_Y);
		}
		if (auxPos.getY() < -Constants.LIM_Y){
			this.getSpeed().setY(-this.getSpeed().getY());
			auxPos.setY(-Constants.LIM_Y);
		}
		if (auxPos.getZ() > Constants.LIM_Z){
			this.getSpeed().setZ(-this.getSpeed().getZ());
			auxPos.setZ(Constants.LIM_Z);
		}
		if (auxPos.getZ() < 0){
			this.getSpeed().setZ(-this.getSpeed().getZ());
			auxPos.setZ(0);
		}
		this.setPosition(auxPos);
	}
	public abstract void drawModel();
	//Abstract Method to be Filled by Children

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		glPushMatrix();
		glTranslatef(getPosition().getX(), getPosition().getY(), getPosition().getZ());
		drawModel();
		glPopMatrix();
	}
}
