package asteroid;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.util.glu.GLU.gluLookAt;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import tools.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Renderer extends Thread {
	private Game world;			//Single Client World
	private Game serverWorld;	//Server World. Can be null
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight; 
	private FloatBuffer lModelAmbient;
	private Texture texture;
	
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
        //glLoadIdentity();
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
	
	public void initTextureBG(){
		try {
			this.texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/bg.jpg"));
			 
		} catch (IOException ex) {
			System.out.println("Problem Loadig Texture");
		}
	}
	
	public void initOGL(){
		   
		glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);

		glClearDepth(1.0f); 			// clear depth buffer
        glEnable(GL_DEPTH_TEST); 		// Enables depth testing
        glDepthFunc(GL_LEQUAL); 		// sets the type of test to use for depth
        glMatrixMode(GL_PROJECTION); 	// sets the matrix mode to project
		glLoadIdentity();
		glViewport(0, 0, Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT);
		gluPerspective(45.0f, (float) Constants.DISPLAY_WIDTH / (float) Constants.DISPLAY_HEIGHT, 0.01f, 1000.0f);
		//Set initial camera position and foa
		gluLookAt(  this.world.getCameraPosition().getX(), this.world.getCameraPosition().getY(), this.world.getCameraPosition().getZ(),   	// Camera Position
					this.world.getCameraFoa().getX(), this.world.getCameraFoa().getY(), this.world.getCameraFoa().getZ(),   				// FOA
					0, 1, 0 );
		initLightOGL();
		initTextureBG();
		
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
	
	private void updateCamera(){
		float moveX = 0f;
		float moveY = 0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			moveX = moveX + 0.5f;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			moveX = moveX - 0.5f;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			moveY = moveY - 0.5f;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			moveY = moveY + 0.5f;
		}
		if (moveX != 0 || moveY != 0){
			GL11.glRotatef(moveX, 1,0,0);
			GL11.glRotatef(moveY, 0,1,0);
		}
	}
	
	private void render() {
		while (!Display.isCloseRequested()  && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			//Get new camera position from keyboard
			updateCamera();
			//Draw
			if (Display.isVisible()) {		
				glPushMatrix();
				draw();
				glPopMatrix();
				
			}
			//
			Display.update();
			Display.sync(60);
		}
		world.setActive(false);
		if (this.getServerWorld() != null)
		{
			this.getServerWorld().setActive(false);
		}
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
	
	public void drawBG(){
		Color.white.bind();
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 1.0f); 
			glVertex3f(-Constants.LIM_X-1f, -Constants.LIM_Y-1f, 0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f( Constants.LIM_X+1f, -Constants.LIM_Y-1f, 0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f( Constants.LIM_X+1f, Constants.LIM_Y+1f, 0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-Constants.LIM_X-1f, Constants.LIM_Y+1f, 0f);	// Top Left Of The Texture and Quad
		glEnd();
		glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public void drawLines(){
		//Texture BG
		drawBG();
		//Dark grey on right side of screen
        glColor3f(0.4f, 0.4f, 0.4f);
        glBegin(GL11.GL_QUADS);
        	glVertex3d(Constants.LIM_X+1, Constants.LIM_Y+1,0f);
        	glVertex3d(Constants.LIM_X+1, Constants.LIM_Y+1, 100f);
        	glVertex3d(Constants.LIM_X+1, -Constants.LIM_Y-1,100f);
        	glVertex3d(Constants.LIM_X+1, -Constants.LIM_Y-1, 0f);
        glEnd();
      //Dark grey on left side of screen
        glBegin(GL11.GL_QUADS);
        	glVertex3d(-Constants.LIM_X-1, -Constants.LIM_Y-1,0f);
        	glVertex3d(-Constants.LIM_X-1, -Constants.LIM_Y-1, 100f);
        	glVertex3d(-Constants.LIM_X-1, Constants.LIM_Y+1,100f);
        	glVertex3d(-Constants.LIM_X-1, Constants.LIM_Y+1, 0f);
        glEnd();
      //Light grey on top side of screen
        glColor3f(0.8f, 0.8f, 0.8f);
        glBegin(GL11.GL_QUADS);
        	glVertex3d(Constants.LIM_X+1, Constants.LIM_Y+1,0f);
        	glVertex3d(Constants.LIM_X+1, Constants.LIM_Y+1, 100f);
        	glVertex3d(-Constants.LIM_X-1, Constants.LIM_Y+1,100f);
        	glVertex3d(-Constants.LIM_X-1, Constants.LIM_Y+1, 0f);
        glEnd();
      //Light grey on bottom side of screen
        glBegin(GL11.GL_QUADS);
        	glVertex3d(-Constants.LIM_X-1, -Constants.LIM_Y-1,0f);
        	glVertex3d(-Constants.LIM_X-1, -Constants.LIM_Y-1, 100f);
        	glVertex3d(Constants.LIM_X+1, -Constants.LIM_Y-1,100f);
        	glVertex3d(Constants.LIM_X+1, -Constants.LIM_Y-1, 0f);
        glEnd();
	}
}