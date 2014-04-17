package dblockcache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import common.Constants;

import virtualdisk.DeVirtualDisk;
import virtualdisk.VirtualDisk;

public class DeDBufferCache extends DBufferCache {

	Hashtable<Integer, DBuffer> myCacheTable = new Hashtable<Integer, DBuffer>();
	VirtualDisk myVirtualDisk;
	
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
			return myCacheTable.get(i);
		} else {
			// allocate a dbuffer
			return new DeDBuffer(blockID);
		}
	}

	@Override
	public void releaseBlock(DBuffer buf) {
		myCacheTable.remove(buf);
	}

	@Override
	/* Write back all dirty blocks to the volume, and wait for completion. 
	*/
	public void sync() {
		// Calls startFetch/startPush
		
	}
	
	private boolean isFull() {
		if (myCacheTable.size() > Constants.NUM_OF_CACHE_BLOCKS) {
			return true;
		}
		return false;
	}

}
