package tools;

import java.io.Serializable;

public class Constants implements Serializable{
	
	private static final long serialVersionUID = -1609863545437900988L;
	
	public static final int DISPLAY_WIDTH =  800;
	public static final int DISPLAY_HEIGHT = 600;	
	public static final boolean FULLSCREEN = false;
	public static final int SERVER_PORT = 1234;
	public static final String SERVER_ADDRESS = "127.0.0.1";
	public static final float LIM_X = 60f;
	public static final float LIM_Y = 40f;
	public static final float LIM_Z = 50f;
	public static final int CLIENT_ASTEROIDS = 10;
	public static final float DEFAULT_SIZE = 2f; 
	public static final float MAX_SPEED = 0.1f;
}
