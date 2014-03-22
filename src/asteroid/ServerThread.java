package asteroid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;

public class ServerThread implements Runnable{
	
	private Socket conection;
	private Game serverWorld;
	private Hashtable<Integer, Long> clients;
	
	public ServerThread(Socket conection, Game world, Hashtable<Integer, Long> clients){
		this.conection = conection;
		this.serverWorld = world;
		this.clients = clients;
	}
	
	public void setConection(Socket conection){
		this.conection = conection;
	}
	
	public void setServerWorld(Game world){
		this.serverWorld = world;
	}
	
	public Game getServerWorld(){
		return this.serverWorld;
	}
	
	public Socket getConection(){
		return this.conection;
	}
	
	public Hashtable<Integer, Long> getClients(){
		return this.clients;
	}
	
	public void managePetition(Message message){
		this.getServerWorld().refreshClient(message.getClientId());
		for (int i = 0; i < message.getAsteroids().size(); i++){
			this.getServerWorld().addObject(message.getAsteroids().get(i));
		}
		this.getClients().put(message.getClientId(), System.nanoTime());
	}
	
	public void run() {
		try
		{
			//ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
	
			ObjectInputStream ois = new ObjectInputStream(this.getConection().getInputStream());
			while(this.getServerWorld().isActive())
			{	
				Message message = (Message) ois.readObject();
				if (message == null){
					break;
				}
				managePetition(message);
			}
			ois.close();
			this.getConection().close();
			//If exited because we closed server world, exit
			//If not, it was just a client leaving. Do nothing else
			if (!this.getServerWorld().isActive()){
				System.exit(-1);
			}
		}
		catch(SocketException se)
		{
            System.out.println("We have lost the conection with a Client");
		}
		catch(EOFException eofe)
		{
			System.out.println("We have lost the conection with a Client");
		}
		catch(IOException ioe)
		{
			System.out.println("IO Error");
			ioe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Wrong Class sent");
			cnfe.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
