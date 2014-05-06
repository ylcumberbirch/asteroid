package asteroid;

public class Control extends Thread{
	
	private Game world;
	private long time;
	
	public Control(Game world){
		this.world = world;
		this.time = System.nanoTime();
	}
	
	public Game getWorld(){
		return this.world;
	}
	
	public long getTime(){
		return this.time;
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
	public void run(){
		while(this.getWorld().isActive())
		{
			this.setTime(System.nanoTime());
			this.getWorld().update(this.getTime());			
			try{
				Thread.sleep(3);
			}catch (InterruptedException ex) {
			}
		}
		
	}

}
