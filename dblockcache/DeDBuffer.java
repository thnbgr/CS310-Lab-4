package dblockcache;

import java.io.IOException;
import virtualdisk.*;
import common.*;


public class DeDBuffer extends DBuffer {

	/*
	 * The Dbuffer maintains status information about the block, e.g., whether
	 * or not device I/O is in progress on the block. Other Dbuffer methods
	 * allow a thread to wait on a Dbuffer for pending I/O to complete.
	 */

	/* Have some sort of data structure to store metadata */

	VirtualDisk myVirtualDisk;
	private int myBlockID;
	private byte[] myBuffer;
	protected boolean isValid;
	protected boolean isDirty;
	public boolean isBusy;

	public DeDBuffer(int i, VirtualDisk v) {
		myBuffer = new byte[Constants.BLOCK_SIZE];
		myBlockID = i;
		isDirty = false;
		isBusy = false;
		myVirtualDisk =  v;
	}

	@Override
	public synchronized void startFetch() {
		
		isValid = false;
 		System.out.println("    Starting Fetch for Block "+myBlockID);
		try {
			myVirtualDisk.startRequest(this, Constants.DiskOperationType.READ);
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
 		System.out.println("    Waiting Fetch for Block "+myBlockID);
		try {
			waitValid();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void startPush() {
		
		try {
			myVirtualDisk.startRequest(this, Constants.DiskOperationType.WRITE);
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		// When it completes pushing:
		// set the dirty condition to return false
		try {
			waitClean();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkValid() {
		
		return isValid;
	}

	@Override
	public synchronized boolean waitValid() throws InterruptedException {
		
		// calls virtualDisk fetch method, which will notify the following wait
		// wait
		while(!checkValid())
		{
			wait();
		}
		return true;
	}

	@Override
	public boolean checkClean() {
		return !isDirty;
	}

	@Override
	public synchronized boolean waitClean() throws InterruptedException {
		
		while(!checkClean())
		{
			try {
				wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public boolean isBusy() {
		
		return isBusy;
	}

	@Override
	public synchronized int read(byte[] buffer, int startOffset, int count) {
		
		// check valid
		try {
			waitValid();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		// read data
		for (int i=0; i<count; i++) {
			buffer[startOffset] = myBuffer[count];
			startOffset++;
		}
		return 0;
	}

	@Override
	public synchronized int write(byte[] buffer, int startOffset, int count) {
		
		// check valid
		try {
			waitValid();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		// read data
		for (int i=0; i<count; i++) {
			myBuffer[count] = buffer[startOffset];
			startOffset++;
		}
		isDirty = true;
		return 0;
	}

	@Override
	/* called when an IO is completed by VirtualDisk (?) */
	// This is what notifies
	public synchronized void ioComplete() {
		isValid = true;
		isDirty = false;
		notifyAll();
	}

	@Override
	public int getBlockID() {
		return myBlockID;
	}

	@Override
	public byte[] getBuffer() {
		return myBuffer;
	}

	public byte[] getBlockContents() {
		byte[] b = new byte[Constants.BLOCK_SIZE];
		read(b, 0, Constants.BLOCK_SIZE);
		return b;
	}

}
