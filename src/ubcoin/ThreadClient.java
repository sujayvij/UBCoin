package ubcoin;

public class ThreadClient implements Runnable {
	private Thread t;
	   private String threadName;
	/**
	 * @param args
	 */
	   
	   public void start () {
		      System.out.println("Starting " +  threadName );
		      if (t == null) {
		         t = new Thread (this, threadName);
		         t.start ();
		      }
		   }
	   
	ThreadClient( String name) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	   }
	public void run()  {
		// TODO Auto-generated method stub
		//MultiThreadChatClient.begin();
	}

}
