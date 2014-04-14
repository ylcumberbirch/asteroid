/**
 * 
 */
package asteroid;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import tools.Constants;

/**
 * @author yazbod
 *
 */
public class Client {

	/**
	 * @param args
	 */
	public Client(){
	}
	
	public static void main(String[] args) {
		int clientID = (int)(Math.floor(1000000*Math.random()));
		
		Game myWorld = new Game(Constants.CLIENT_ASTEROIDS, clientID);
		Renderer renderer = new Renderer(myWorld);
		renderer.start();
		Control control = new Control(myWorld);
		control.start();
		try{
			Socket socket = new Socket(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);
			SocketChannel sc = socket.getChannel();
			//ByteBuffer bb = new ByteBuffer();
			//bb.append(1);
			//sc.write(bb);
		    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		    oos.flush();
			while(myWorld.isActive()) {
				//Create Message and send
				Message message= new Message(clientID, myWorld.getObjects());
				oos.writeObject(message);
				oos.flush();
				oos.reset();
				//Sleep
				try {
					Thread.sleep(10);
				} catch (InterruptedException ex) {
				}
			}
			//Window Closed. Close socket and oos
			oos.writeObject(null);
			oos.close();
			socket.close();
			
	    } catch (SocketException se) {
			System.out.println("Server not found");
			System.exit(-1);
		} catch(UnknownHostException uhe) {
			System.out.println("Error at connecting with server: unknown host");
		} catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
