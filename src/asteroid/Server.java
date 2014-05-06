package asteroid;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tools.Constants;

public class Server extends Thread{
	
	private static ExecutorService pool;
	private Game serverworld;
	private Game myWorld;
	private Hashtable<Integer, Long> clients;
	
	public Server(Game world, Game myWorld){
		this.serverworld = world;
		this.myWorld = myWorld;
		this.clients = new Hashtable<Integer, Long>();
	}
	
	public Game getServerWorld(){
		return this.serverworld;
	}
	
	public Hashtable<Integer, Long> getClients(){
		return this.clients;
	}
	
	public Game getMyWorld(){
		return this.myWorld;
	}
	
	public void setServerWorld(Game world){
		this.serverworld = world;
	}
	
	public void run()
	{
		try
		{
			ServerControl sc = new ServerControl(this.getServerWorld(), this.getClients());
			sc.start();
			pool = Executors.newFixedThreadPool(10);
			InetAddress addr = InetAddress.getByName(Constants.SERVER_ADDRESS);
			ServerSocket server = new ServerSocket(Constants.SERVER_PORT, 5, addr);
			System.out.println("Server listening at " + server.getInetAddress().toString() + ":" + server.getLocalPort());
			
			while (this.getServerWorld().isActive())
			{
				Socket conection = server.accept();
				ServerThread request = new ServerThread(conection, this.getServerWorld(), this.getClients());
				pool.execute(request);
			}
			server.close();
			pool.shutdown();
			System.exit(-1);
		}
		catch(IOException ioe)
		{
			System.out.println("IO Error");
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

}
