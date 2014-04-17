package dblockcache;

import java.util.Hashtable;

import virtualdisk.DeVirtualDisk;
import virtualdisk.VirtualDisk;

public class DeDBufferCache extends DBufferCache {

	Hashtable<Integer, DBuffer> myCacheTable = new Hashtable<Integer, DBuffer>();
	VirtualDisk myVirtualDisk;
	public DeDBufferCache(int cacheSize) {
		super(cacheSize);
		myVirtualDisk = new DeVirtualDisk();
	}

	@Override
	/* Finds the DBuffer associated with the blockID,
	 * or allocates a DBuffer if it isn't in the cache
	 * Once the DBuffer exists, it requests the virtualDisk
	 * to read block contents into buffer, or write
	 * contents of buffer into block in virtualDisk */
	
	public DBuffer getBlock(int blockID) {
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

}
