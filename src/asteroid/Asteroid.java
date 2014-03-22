/**
 * 
 */
package asteroid;

import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import tools.Vector;

/**
 * @author yazbod
 *
 */
public class Asteroid extends GameObject{

	private static final long serialVersionUID = 2345505974888697407L;
	private static final int NUM_FACETS = 20;
	private float size;
	private Vector color;
	
	public Asteroid(float size, int clientID){
		super(clientID);
		this.size = size;
		this.color = new Vector(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat());
	}
	
	
	public void setSize(int size){
		this.size = size;
	}
	
	public float getSize(){
		return this.size;
	}
	
	public Vector getColor(){
		return this.color;
	}
	
	public void setColor(Vector color){
		this.color = color;
	}
	
	public float getR(){
		return this.getColor().getX();
	}
	
	public float getG(){
		return this.getColor().getY();
	}
	
	public float getB(){
		return this.getColor().getZ();
	}
	
	public void drawModel(){
		renderAsteroid(NUM_FACETS);
	}
	
	public void renderAsteroid(int facets){
		GL11.glColor3f(this.getR(), this.getG(), this.getB());
		Sphere s = new Sphere();            // an LWJGL class
        s.setOrientation(GLU.GLU_OUTSIDE);  // normals point outwards
        s.setTextureFlag(false);             // generate texture coords
        GL11.glPushMatrix();
        {
	        //GL11.glRotatef(-90f, 1,0,0);    // rotate the sphere to align the axis vertically
        	
	        s.draw(this.getSize(), facets, facets); // run GL commands to draw sphere
        }
        GL11.glPopMatrix();
	}
}
