package virtualdisk;


import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import common.LogWriter;
import common.Constants.DiskOperationType;
import dblockcache.DBuffer;

public class DeVirtualDisk extends VirtualDisk {

	Queue<RequestBuffer> requests = new LinkedList<RequestBuffer>();
	DeVirtualDiskThread myDiskThread = new DeVirtualDiskThread(this);
	
	public DeVirtualDisk(String volName, boolean format)
			throws FileNotFoundException, IOException {
		super(volName, format);
		myDiskThread.start();
	}

	@Override
	public synchronized void startRequest(DBuffer buf, DiskOperationType operation)
			throws IllegalArgumentException, IOException {
	
		RequestBuffer r = new RequestBuffer(buf, operation);
		requests.add(r);
		notifyAll();
	}

	public synchronized void handleRequests() throws InterruptedException, IOException{
	
		while(requests.isEmpty())
		{
			wait();
		}
		
		RequestBuffer r = requests.remove();
		
	    if (r.myOperation == DiskOperationType.READ) {
		    int a = readBlock(r.mybuff);
			long start = System.nanoTime();
			LogWriter.log("  Fetched for block #" + r.mybuff.getBlockID(), start);
	    } else if (r.myOperation == DiskOperationType.WRITE) {
		    writeBlock(r.mybuff);
			long start = System.nanoTime();
			LogWriter.log("  Pushed for block #" + r.mybuff.getBlockID(), start);
	    }
	    r.mybuff.ioComplete();
	}
}
