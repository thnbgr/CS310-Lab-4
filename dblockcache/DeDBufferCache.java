package dblockcache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import virtualdisk.*;
import common.*;

public class DeDBufferCache extends DBufferCache {
	
 
	Hashtable<Integer, DeDBuffer> myCacheTable = new Hashtable<Integer, DeDBuffer>();
	LinkedList<Integer> myBufferQueue = new LinkedList<Integer>();
	static VirtualDisk myVirtualDisk;
	private int numBuffers = Constants.NUM_OF_CACHE_BLOCKS;
	
	public DeDBufferCache(int cacheSize) {
		super(cacheSize);
		try {
			myVirtualDisk = new DeVirtualDisk(Constants.vdiskName, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	/* Finds the DBuffer associated with the blockID,
	 * or allocates a DBuffer if it isn't in the cache
	 * Once the DBuffer exists, it requests the virtualDisk
	 * to read block contents into buffer, or write
	 * contents of buffer into block in virtualDisk */
	
	public synchronized DBuffer getBlock(int blockID) {
 		Integer i = blockID;
 		DeDBuffer returnBuffer = new DeDBuffer(i, myVirtualDisk);
 		if (myCacheTable.containsKey(i)) {
			returnBuffer = myCacheTable.get(i);
			myBufferQueue.remove(i);
			myBufferQueue.add(i);
		}
		else {
			if (isFull()) {
				DeDBuffer evictBuffer = myCacheTable.get(myBufferQueue.get(0));
				while (evictBuffer.isBusy()) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Integer evictID = evictBuffer.getBlockID();
				if (!evictBuffer.checkClean()) {
					evictBuffer.startPush();
				}
				myCacheTable.remove(evictID);
				myBufferQueue.remove(evictID);
			}
			myCacheTable.put(i, returnBuffer);
			myBufferQueue.add(i);
			returnBuffer.startFetch();

		}
		while (returnBuffer.isBusy()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		returnBuffer.isBusy = true;
		return returnBuffer;
	}

	@Override
	public synchronized void releaseBlock(DBuffer buf) {
		DeDBuffer buffer = (DeDBuffer) buf;
		buffer.isBusy = false;
		notifyAll();
	}

	@Override
	/* Write back all dirty blocks to the volume, and wait for completion. 
	*/
	public synchronized void sync() {
		// Calls startFetch/startPush
		int queueSize = myBufferQueue.size();
		for(DeDBuffer buff : myCacheTable.values())
		{
			if(buff.isDirty)
			{
				buff.startPush();
			}
			if(!buff.isValid)
			{
				buff.startFetch();
			}
		}
		// Check all buffers for clean/dirty
		// Push all dirty buffers, wait for completion
		
	}
	
	private boolean isFull() {
		if (myCacheTable.size() > Constants.NUM_OF_CACHE_BLOCKS-1) {
			return true;
		}
		return false;
	}
	
	public Hashtable<Integer, DeDBuffer> getCacheTable() {
		return myCacheTable;
	}

}
