package dfs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		// First, get inode
		DeDBuffer inodeBuffer = getInodeBuffer(dFID);
		byte[] content = inodeBuffer.getBuffer(); // get the whole block

		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(content);
			bfReader = new BufferedReader(new InputStreamReader(is));
			Integer temp;
			int size = bfReader.read(); // reads first int of inode block
			int offset = 0;
			buffer = new byte[size]; // instantiates byte array of size of file
			while ((temp = bfReader.read()) != null) { // Reading from inode
				DeDBuffer b = (DeDBuffer) myBufferCache.getBlock(temp); // Read each block
				byte[] tempBuffer = b.getBuffer();
				int tempLength = tempBuffer.length;
				offset += tempLength;
				System.arraycopy(tempBuffer, 0, buffer, offset, tempLength); // writes to return buffer
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {
			}
		
		}

		return 0;
	}

	@Override
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// TODO Auto-generated method stub
		// call updateInode()
		return 0;
	}

	private DeDBuffer getInodeBuffer(DFileID dFID) {
		DeDBuffer b = (DeDBuffer) myBufferCache.getBlock(dFID.getDFileID());
		return b;
	}

	@Override
	public int sizeDFile(DFileID dFID) {
		DeDBuffer b = (DeDBuffer) myBufferCache.getBlock(dFID.getDFileID());
		byte[] buffer = null;
		b.read(buffer, 0, 4);
		ByteBuffer wrapped = ByteBuffer.wrap(buffer);
		int num = wrapped.getInt();
		return num;
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
