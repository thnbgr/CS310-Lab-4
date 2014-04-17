package dblockcache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import virtualdisk.*;
import common.*;

public class DeDBufferCache extends DBufferCache {
	

	Hashtable<Integer, DeDBuffer> myCacheTable = new Hashtable<Integer, DeDBuffer>();
	LinkedList<Integer> myBufferQueue = new LinkedList<Integer>();
	VirtualDisk myVirtualDisk;
	private int numBuffers = Constants.NUM_OF_CACHE_BLOCKS;
	
	public DeDBufferCache(int cacheSize) {
		super(cacheSize);
		try {
			myVirtualDisk = new DeVirtualDisk("disk", true);
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
	
	public DBuffer getBlock(int blockID) {
		if (isFull()) {
			// deal with eviction
		}
		
 		Integer i = blockID;
		if (myCacheTable.contains(i)) {
			DeDBuffer calledBuffer = myCacheTable.get(i);
			myBufferQueue.remove(i);
			myBufferQueue.add(i);
			return calledBuffer;
		}
		else {
			DeDBuffer newBuffer = new DeDBuffer(i);
			if (myCacheTable.size() == numBuffers) {
				DeDBuffer evictBuffer = myCacheTable.get(myBufferQueue.get(0));
				Integer evictID = evictBuffer.getBlockID();
				if (!evictBuffer.checkClean()) {
					evictBuffer.startPush();
				}
				this.releaseBlock(evictBuffer);
				myBufferQueue.remove(evictID);
			}
			myCacheTable.put(i, newBuffer);
			myBufferQueue.add(i);
			return newBuffer;
		}
	}

	@Override
	public void releaseBlock(DBuffer buf) {
		int evictID = buf.getBlockID();
		myCacheTable.remove(evictID);
	}

	@Override
	/* Write back all dirty blocks to the volume, and wait for completion. 
	*/
	public void sync() {
		// Calls startFetch/startPush
		// Check all buffers for clean/dirty
		// Push all dirty buffers, wait for completion
		
	}
	
	private boolean isFull() {
		if (myCacheTable.size() > Constants.NUM_OF_CACHE_BLOCKS) {
			return true;
		}
		return false;
	}

}
