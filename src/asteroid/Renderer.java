package asteroid;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.util.glu.GLU.gluLookAt;

import org.lwjgl.BufferUtils;
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
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight; 
	private FloatBuffer lModelAmbient;
	
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
        drawLines();
		this.getWorld().draw();
		if (this.getServerWorld() != null){
			this.getServerWorld().draw();
		}
	}
	
	public void stopRendering() {
		//Methods already check if created before destroying.
		Display.destroy();
		Keyboard.destroy();
		System.exit(0);
	}
	
	private void initLightArrays() {
		matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
		
		whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
	}
	
	public void initOGL(){
		glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH);

		glClearDepth(1.0f); // clear depth buffer
        glEnable(GL_DEPTH_TEST); // Enables depth testing
        glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth
        glMatrixMode(GL_PROJECTION); // sets the matrix mode to project
		//glLoadIdentity();
		glViewport(0, 0, Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT);
		gluPerspective(45.0f, (float) Constants.DISPLAY_WIDTH / (float) Constants.DISPLAY_HEIGHT, 0.01f, 1000.0f);
		
		gluLookAt(  this.world.getCameraPosition().getX(), this.world.getCameraPosition().getY(), this.world.getCameraPosition().getZ(),   	// Camera Position
					this.world.getCameraFoa().getX(), this.world.getCameraFoa().getY(), this.world.getCameraFoa().getZ(),   				// FOA
					0, 1, 0 );
		initLightOGL();
		
	}
	
	public void initLightOGL() {
		glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glLoadIdentity();
		initLightArrays();
		glShadeModel(GL_SMOOTH);
		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
		
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
		
		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0
		
		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
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
	public void drawLines(){
		glLineWidth(2);
		glBegin(GL_LINES);
			glColor3f(0, 0, 0);
			//Top Line
        	glVertex3d(Constants.LIM_X, Constants.LIM_Y,0f);
            glVertex3d(-Constants.LIM_X, Constants.LIM_Y, 0f);
            //Bottom Line
            glVertex3d(Constants.LIM_X, -Constants.LIM_Y,0f);
            glVertex3d(-Constants.LIM_X, -Constants.LIM_Y, 0f);
            //Right Line
            glVertex3d(Constants.LIM_X, -Constants.LIM_Y,0f);
            glVertex3d(Constants.LIM_X, Constants.LIM_Y, 0f);
            //Left Line
            glVertex3d(-Constants.LIM_X,-Constants.LIM_Y,0f);
            glVertex3d(-Constants.LIM_X, Constants.LIM_Y, 0f);
            //Diag Top Right
        	glVertex3d(Constants.LIM_X, Constants.LIM_Y,0f);
            glVertex3d(Constants.LIM_X, Constants.LIM_Y, 100f);
            //Diag Top Left
            glVertex3d(-Constants.LIM_X, Constants.LIM_Y,0f);
            glVertex3d(-Constants.LIM_X, Constants.LIM_Y, 100f);
            //Diag Bottom Right
            glVertex3d(Constants.LIM_X, -Constants.LIM_Y,0f);
            glVertex3d(Constants.LIM_X, -Constants.LIM_Y, 100f);
            //Diag Bottom Left
            glVertex3d(-Constants.LIM_X, -Constants.LIM_Y,0f);
            glVertex3d(-Constants.LIM_X, -Constants.LIM_Y, 100f);
        glEnd();
	}
}