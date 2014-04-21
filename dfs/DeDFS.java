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
	ArrayList<Integer> myFilledBlockIDs;

	@Override
	public void init() {
		myDFileIDs = new DFileID[Constants.MAX_DFILES];
		myBufferCache = new DeDBufferCache(Constants.NUM_OF_CACHE_BLOCKS);

		myFilledBlockIDs = new ArrayList<Integer>();

		for (int i = 0; i < Constants.MAX_DFILES; i++) {
			myFilledBlockIDs.add(i);
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
				DeDBuffer inodeBuffer = getInodeBuffer(myDFileIDs[i]);
				byte[] blockArray = new byte[Constants.BLOCK_SIZE];
				Arrays.fill(blockArray, (byte) 0);
				inodeBuffer.write(blockArray, 0, Constants.BLOCK_SIZE);
				myFilledBlockIDs.remove(i);
				myDFileIDs[i] = null;
			}
		}
	}

	@Override
	public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// First, get inode
		DeDBuffer inodeBuffer = getInodeBuffer(dFID);
		byte[] content = inodeBuffer.getBlockContents(); // get the whole block

		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(content);
			bfReader = new BufferedReader(new InputStreamReader(is));
			Integer temp;
			int size = bfReader.read(); // reads first int of inode block
			int offset = startOffset;
			buffer = new byte[size]; // instantiates byte array of size of file
			while ((temp = bfReader.read()) != null) { // Reading from inode
				// Read each block
				DeDBuffer b = (DeDBuffer) myBufferCache.getBlock(temp);
				System.out.println("Got Block:" + b.getBlockID());
				byte[] tempBuffer = b.getBlockContents();
				int tempLength = tempBuffer.length;
				offset += tempLength;
				// writes to return buffer
				System.arraycopy(tempBuffer, 0, buffer, offset, tempLength);

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

	public byte[] intToByteArray(int i) {
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(i);
		byte[] d = c.array();
		return d;
	}

	@Override
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
		byte[] blockArray = new byte[Constants.BLOCK_SIZE];
		Arrays.fill(blockArray, (byte) 0);
		DeDBuffer inodeBuffer = (DeDBuffer) myBufferCache.getBlock(dFID
				.getDFileID());
		System.out.println("Creating INode for file "+inodeBuffer.getBlockID());
		int inodeOffset = 0;
		int size = sizeDFile(dFID);
		byte[] b = null;
		if (count > size) {
			// rewrite inode file size
			b = intToByteArray(count);
			System.arraycopy(b, 0, blockArray, inodeOffset, b.length);
		} else {
			// rewrite file size
			b = intToByteArray(size);
			System.arraycopy(b, 0, blockArray, inodeOffset, b.length);
		}
		inodeOffset += b.length;

		int fileOffset = startOffset;
		System.out.println("Writing blocks...");
		for (int i = 0; i < count; i += Constants.BLOCK_SIZE) {
			int blockNumber = getFreeBlockID();
			DeDBuffer currentBlockBuffer = (DeDBuffer) myBufferCache
					.getBlock(blockNumber);
			// write inodeBlockID to inode block map
			int inodeBlockID = currentBlockBuffer.getBlockID();
			b = intToByteArray(inodeBlockID);
			System.arraycopy(b, 0, blockArray, inodeOffset, b.length);
			inodeOffset += b.length;
			// write buffer contents to currentBlockBuffer
			// contains block segment of file
			byte[] blockContents = new byte[Constants.BLOCK_SIZE];
			// copies BLOCK_SIZE array from buffer
			System.arraycopy(buffer, i, blockContents, 0, Constants.BLOCK_SIZE);
			// writes contents to block
			currentBlockBuffer.write(blockContents, 0, Constants.BLOCK_SIZE);
			inodeOffset += Constants.BLOCK_SIZE;
		}
		// write the whole inode
		inodeBuffer.write(blockArray, 0, blockArray.length);
		return 0;
	}

	/*
	 * What keeps track of the free blocks?
	 */
	private int getFreeBlockID() {
		for (int i = Constants.MAX_DFILES; i < Constants.BLOCK_SIZE; i++) {
			if (!myFilledBlockIDs.contains(i)) {
				return i;
			}
		}
		return -1;
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
		myBufferCache.sync();

	}

}