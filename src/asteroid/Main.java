package asteroid;

import tools.Constants;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int clientID = (int)(Math.floor(1000000*Math.random()));
		Game serverWorld = new Game(0,0);								//World with all asteroids
		Game myWorld = new Game(Constants.CLIENT_ASTEROIDS, clientID);	//World with the initial clients asteroids only
		Server server = new Server(serverWorld, myWorld);				//Create Server
		server.start();													//Get server running
		Renderer rendererS = new Renderer(serverWorld, myWorld);		//Renderer ServerWorld
		Control controlM = new Control(myWorld);						//Control myWorld
		controlM.start();												//Start Control												//Start Rendering my World
		rendererS.start();												//Start Rendering server World
	}
}
