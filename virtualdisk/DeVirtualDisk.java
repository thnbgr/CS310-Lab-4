package virtualdisk;


import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import common.Constants.DiskOperationType;
import dblockcache.DBuffer;

public class DeVirtualDisk extends VirtualDisk {

	Queue<RequestBuffer> requests = new LinkedList<RequestBuffer>();
	
	public DeVirtualDisk(String volName, boolean format)
			throws FileNotFoundException, IOException {
		super(volName, format);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startRequest(DBuffer buf, DiskOperationType operation)
			throws IllegalArgumentException, IOException {
	
		RequestBuffer r = new RequestBuffer(buf, operation);
		requests.add(r);
		
		
		
		/*	if (operation == DiskOperationType.READ) {
			int r = readBlock(buf);
		} else if (operation == DiskOperationType.WRITE) {
			writeBlock(buf);
		}
		 */
		// Somewhere this happens
		buf.ioComplete();
	}

	public void handleRequests() throws InterruptedException, IOException{
	
		while(requests.isEmpty())
		{
			wait();
		}
		
		RequestBuffer r = requests.remove();
		
	    if (r.myOperation == DiskOperationType.READ) {
		    int a = readBlock(r.mybuff);
	    } else if (r.myOperation == DiskOperationType.WRITE) {
		    writeBlock(r.mybuff);
	    }
	}
}
