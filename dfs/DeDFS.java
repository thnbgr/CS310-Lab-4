package dfs;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Constants;
import common.DFileID;
import dblockcache.DeDBuffer;
import dblockcache.DeDBufferCache;

public class DeDFS extends DFS {

	DeDBufferCache myBufferCache;
	DFileID[] myDFileIDs;
	
	@Override
	public void init() {
		myDFileIDs = new DFileID[Constants.MAX_DFILES];
		myBufferCache = new DeDBufferCache(Constants.NUM_OF_CACHE_BLOCKS);
		
		for (int i = 0; i < Constants.MAX_DFILES; i++) {
			DeDBuffer buffer = (DeDBuffer) myBufferCache.getBlock(i);
			// First int is the file size
			int currentOffset = 0;
			ByteBuffer b = ByteBuffer.allocate(4);
			b.putInt(0);
			byte[] buf = b.array();
			buffer.write(buf, currentOffset, buf.length);
			currentOffset += buf.length;
			// Write an int for each of the 500 blocks
			for (int j = 0; j < Constants.MAX_NUM_BLOCKS_PER_FILE; j++) {
				ByteBuffer c = ByteBuffer.allocate(4);
				c.putInt(0);
				byte[] d = b.array();
				buffer.write(d, currentOffset, d.length);
				currentOffset += d.length;
			}
			// Done allocating the inode
			
		}
	}

	@Override
	public DFileID createDFile() {
		DFileID r = null;
		for (int i = 0; i < myDFileIDs.length; i++) {
			if (myDFileIDs[i] == null) {
				r = new DFileID(i);
				myDFileIDs[i] = r;
			}
		}
		return r;
	}

	@Override
	public void destroyDFile(DFileID dFID) {
		for (int i = 0; i < myDFileIDs.length; i++) {
			if (myDFileIDs[i].equals(dFID)) {
				myDFileIDs[i] = null;
			}
		}
	}
	
	private void updateInode() {
		
	}

	@Override
	public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		// for (int i = 0; i < buffer.length; i+=BLOCK_SIZE) {
			// get a block, write BLOCK_SIZE length of data from buffer, update inode
		return 0;
	}

	@Override
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		// call updateInode()
		return 0;
	}

	@Override
	public int sizeDFile(DFileID dFID) {
		// read first four bytes at dFID.getBlockID()*block size
		return 0;
	}

	@Override
	public List<DFileID> listAllDFiles() {
		return Arrays.asList(myDFileIDs);
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}

}
