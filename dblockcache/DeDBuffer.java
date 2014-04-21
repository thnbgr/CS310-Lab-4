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
		myBlockID = i;
		isDirty = false;
		isBusy = false;
		myVirtualDisk =  v;
	}

	@Override
	public void startFetch() {
		// TODO Auto-generated method stub
		isValid = false;
		
		try {
			myVirtualDisk.startRequest(this, Constants.DiskOperationType.READ);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			waitValid();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void startPush() {
		// TODO Auto-generated method stub
		try {
			myVirtualDisk.startRequest(this, Constants.DiskOperationType.WRITE);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// When it completes pushing:
		// set the dirty condition to return false
		try {
			waitClean();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkValid() {
		// TODO Auto-generated method stub
		return isValid;
	}

	@Override
	public synchronized boolean waitValid() throws InterruptedException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		while(!checkClean())
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return isBusy;
	}

	@Override
	public int read(byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		// check valid
		try {
			waitValid();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// read data
		for (int i=0; i<count; i++) {
			myBuffer[count] = buffer[startOffset];
			startOffset++;
		}
		return 0;
	}

	@Override
	public int write(byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		// check valid
		try {
			waitValid();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// read data
		for (int i=0; i<count; i++) {
			buffer[startOffset] = myBuffer[count];
			startOffset++;
		}
		isDirty = true;
		return 0;
	}

	@Override
	/* called when an IO is completed by VirtualDisk (?) */
	// This is what notifies
	public void ioComplete() {
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
		// TODO Auto-generated method stub
		return null;
	}

}
