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
import java.util.Scanner;

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
				System.out.println("file number: " + i);
				r = new DFileID(i);
				myDFileIDs[i] = r;
				return r;
			}
		}
		return null;
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

	public int fromByteArray(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16
				| (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}

	@Override
	public int read(DFileID dFID, byte[] buffer, int startOffset, int count) {
		// First, get inode
		int intSize = 4;
		DeDBuffer inodeBuffer = getInodeBuffer(dFID);
		System.out.println("got inode buffer number "
				+ inodeBuffer.getBlockID());
		byte[] content = inodeBuffer.getBlockContents(); // get the whole block
		byte[] curBytes = new byte[4];
		System.arraycopy(content, 0, curBytes, 0, intSize);
		int size = fromByteArray(curBytes);
		System.out.println("file size: " + size);
		int offset = startOffset;
		int temp = 0;
		for (int i = intSize; i < content.length; i += intSize) { // Reading
																	// from
																	// inode
			// Read each block
			System.arraycopy(content, i, curBytes, 0, intSize);
			temp = fromByteArray(curBytes);
			if (temp == 0) {
				break;
			}
			System.out.println("current temp:" + temp);
			DeDBuffer b = (DeDBuffer) myBufferCache.getBlock(temp);
			System.out.println("Got Block:" + b.getBlockID());
			byte[] tempBuffer = b.getBlockContents();
			int tempLength = tempBuffer.length;
			// writes to return buffer

			if (buffer.length < tempLength) {
				System.arraycopy(tempBuffer, 0, buffer, offset, buffer.length);
			} else {
				if (buffer.length - offset < tempLength) {
					System.out.println("buffer:"+buffer.length+" offset: "+offset+" templength:"+tempLength);
					System.arraycopy(tempBuffer, 0, buffer, offset, buffer.length - offset);
				} else {
				System.out.println("buffer:"+buffer.length+" offset: "+offset+" templength:"+tempLength);
				System.arraycopy(tempBuffer, 0, buffer, offset, tempLength);
				}
			}
			offset += tempLength;
			myBufferCache.releaseBlock(b);
		}
		myBufferCache.releaseBlock(inodeBuffer);

		return buffer.length;
	}

	public byte[] intToByteArray(int i) {
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(i);
		byte[] d = c.array();
		return d;
	}

	@Override
	public int write(DFileID dFID, byte[] buffer, int startOffset, int count) {
		if (count > Constants.MAX_FILE_SIZE) {
			return -1;
		}
		byte[] blockArray = new byte[Constants.BLOCK_SIZE];
		Arrays.fill(blockArray, (byte) 0);
		System.out.println("Getting INode for file " + dFID.getDFileID());
		DeDBuffer inodeBuffer = (DeDBuffer) myBufferCache.getBlock(dFID
				.getDFileID());
		System.out.println("Obtained INode for file "
				+ inodeBuffer.getBlockID());
		int inodeOffset = 0;
		System.out.println("Getting size of file...");
		myBufferCache.releaseBlock(inodeBuffer);
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
		System.out.println("inodeOffset should equal 4: " + inodeOffset);
		int fileOffset = startOffset;
		System.out.println("Writing blocks...");
		for (int bufferOffset = 0; bufferOffset < count; bufferOffset += Constants.BLOCK_SIZE) {

			/* Writing to INODE */
			int blockNumber = getFreeBlockID();
			System.out.println("Free block ID: " + blockNumber);
			DeDBuffer currentBlockBuffer = (DeDBuffer) myBufferCache
					.getBlock(blockNumber);
			// write inodeBlockID to inode block map
			b = intToByteArray(blockNumber);
			System.arraycopy(b, 0, blockArray, inodeOffset, b.length);
			inodeOffset += b.length;

			/* Writing to actual file */
			byte[] blockContents = new byte[Constants.BLOCK_SIZE];

			// copies BLOCK_SIZE array from buffer
			int writeLength = 0;
			if (buffer.length-bufferOffset < Constants.BLOCK_SIZE) {
				writeLength = buffer.length-bufferOffset;
			} else {
				writeLength = Constants.BLOCK_SIZE;
			}
			System.out.println("bufferOffset:"+bufferOffset+" writelength:"+writeLength+" buffersize:"+buffer.length);
			System.arraycopy(buffer, bufferOffset, blockContents, 0, writeLength);
			// writes contents to block
			currentBlockBuffer.write(blockContents, 0, Constants.BLOCK_SIZE);
			myBufferCache.releaseBlock(currentBlockBuffer);
		}
		// write the whole inode
		inodeBuffer.write(blockArray, 0, blockArray.length);
		myBufferCache.releaseBlock(inodeBuffer);
		return 0;
	}

	/*
	 * What keeps track of the free blocks?
	 */
	private int getFreeBlockID() {
		for (int i = Constants.MAX_DFILES; i < Constants.NUM_OF_BLOCKS; i++) {
			if (!myFilledBlockIDs.contains(i)) {
				myFilledBlockIDs.add(i);
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
		byte[] buffer = new byte[4];
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