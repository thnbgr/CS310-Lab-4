package virtualdisk;

import java.io.IOException;

public class DeVirtualDiskThread extends Thread{

	DeVirtualDisk myDisk;
	
	public DeVirtualDiskThread(DeVirtualDisk v)
	{
		myDisk = v;
	}
	
	public void run()
	{
		while(true)
		{
			try {
				myDisk.handleRequests();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}
	
}
