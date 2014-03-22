package asteroid;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.util.glu.GLU.gluLookAt;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import tools.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Renderer extends Thread {
	private Game world;			//Single Client World
	private Game serverWorld;	//Server World. Can be null
	
	public Renderer(Game world){
		this.world = world;
		this.serverWorld = null;
	}
	
	public Renderer(Game serverWorld, Game myWorld){
		this.world = myWorld;
		this.serverWorld = serverWorld;
	}
	
	public Game getWorld(){
		return this.world;
	}
	
	public Game getServerWorld(){
		return this.serverWorld;
	}
	
	public synchronized void draw(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
		this.getWorld().draw();
		if (this.getServerWorld() != null){
			this.getServerWorld().draw();
		}
	}
	
	public void stopRendering() {
		//Methods already check if created before destroying.
		Display.destroy();
		Keyboard.destroy();
	}
	
	public void initOGL(){
		glClearColor(0f, 0f, 0f, 0f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH);
	}
	
	public void resizeOGL() {
		glClearColor(0f, 0f, 0f, 0.5f);

		glClearDepth(1.0f); // clear depth buffer
        glEnable(GL_DEPTH_TEST); // Enables depth testing
        glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth
        glMatrixMode(GL_PROJECTION); // sets the matrix mode to project
		//glLoadIdentity();
		glViewport(0, 0, Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT);
		gluPerspective(60.0f, (float) Constants.DISPLAY_WIDTH / (float) Constants.DISPLAY_HEIGHT, 0.01f, 1000.0f);
		
		gluLookAt(  this.world.getCameraPosition().getX(), this.world.getCameraPosition().getY(), this.world.getCameraPosition().getZ(),   	// Camera Position
					this.world.getCameraFoa().getX(), this.world.getCameraFoa().getY(), this.world.getCameraFoa().getZ(),   				// FOA
					0, 1, 0 );
		glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glLoadIdentity();	
	}
	
	public void initLightOGL() {
		// Place a light source
		ByteBuffer buffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());
		float lpos[] = { 2.0f, 0.0f, 1000.0f, 1.0f };
		float amb[] =  { 0.6f, 0.6f, 0.6f, 1.0f };
		float dif[] =  { 0.8f, 0.8f, 0.8f, 1.0f };
		float spe[] =  { 0.8f, 0.8f, 0.8f, 1.0f };
		glEnable(GL_LIGHT0);
		glLight(GL_LIGHT0, GL_POSITION, (FloatBuffer) buffer.asFloatBuffer().put(lpos).flip());
		glLight(GL_LIGHT0, GL_AMBIENT, (FloatBuffer) buffer.asFloatBuffer().put(amb).flip());
		glLight(GL_LIGHT0, GL_DIFFUSE, (FloatBuffer) buffer.asFloatBuffer().put(dif).flip());
		glLight(GL_LIGHT0, GL_SPECULAR, (FloatBuffer) buffer.asFloatBuffer().put(spe).flip());
	}
	
	private void render() {
		while (!Display.isCloseRequested()  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			if (Display.isVisible()) {
				draw();
			}
			Display.update();
			Display.sync(60);
		}
		world.setActive(false);
		if (this.getServerWorld() != null)
			this.getServerWorld().setActive(false);
	}
	
	public void run() {
		try {
			Display.setDisplayMode(new DisplayMode(Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT));
			Display.setFullscreen(Constants.FULLSCREEN);
			if (this.getServerWorld() == null)
				Display.setTitle("Asteroid");
			else
				Display.setTitle("Server");
			Display.create();
			Keyboard.create();
			world.setActive(true);
			if (this.getServerWorld() != null)
				serverWorld.setActive(true);
			
		} catch (LWJGLException lwjgle) {
			lwjgle.printStackTrace();
		}
		initOGL();
		resizeOGL();
		initLightOGL();	
		if (this.getServerWorld() != null){
			while (serverWorld.isActive()){
				render();
			}
		}
		else{
			while (world.isActive()){
				render();
			}
		}
		stopRendering();
	}
}