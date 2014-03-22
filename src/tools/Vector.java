package tools;

import java.io.Serializable;

public class Vector implements Serializable{
	
	private static final long serialVersionUID = -2736781750879403474L;
	
	private float x;
	private float y;
	private float z;
	
	//Init
	public Vector (float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector (Vector v){
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}
	
	//Setters
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void setZ(float z){
		this.z = z;
	}
	
	//Getters
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getZ(){
		return this.z;
	}
	
	//Other Methods
	public void add(float x, float y, float z){
		/*
		 * Adds the passed parameters to the initial value
		*/
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		this.setZ(this.getZ() + z);
	}
	
	public void add(Vector v){
		this.setX(this.getX() + v.getX());
		this.setY(this.getY() + v.getY());
		this.setZ(this.getZ() + v.getZ());
	}
	
	public void muliply(float x, float y, float z){
		/*
		 * Multiplies the passed parameters to the initial value
		*/
		this.setX(this.getX() * x);
		this.setY(this.getY() * y);
		this.setZ(this.getZ() * z);
	}
	
	public void multiply(Vector v){
		this.setX(this.getX() * v.getX());
		this.setY(this.getY() * v.getY());
		this.setZ(this.getZ() * v.getZ());
	}
	
	public void multiply(float num){ 
		this.setX(this.getX() * num);
		this.setY(this.getY() * num);
		this.setZ(this.getZ() * num);
	}
	
	public void minus(float x, float y, float z){
		/*
		 * Substracts the passed parameters from the initial value
		*/
		this.setX(this.getX() - x);
		this.setY(this.getY() - y);
		this.setZ(this.getZ() - z);
		
	}
	
	public boolean equals (Vector v){
		/*
		 * True id v is equal to this. False if not
		 */
		if (this == v)
			return true;
		if (this.getX() != v.getX())
			return false;
		if (this.getY() != v.getY())
			return false;
		if (this.getZ() != v.getZ())
			return false;
		return true;
		
	}
	
	public void print(){
		
		System.out.println("X: " + this.getX());
		System.out.println("Y: " + this.getY());
		System.out.println("Z: " + this.getZ());
		
		
	}
}
