package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import common.DFileID;
import dfs.*;

public class ReaderThread extends Thread {
	DeDFS myDFS;
	DFileID myID;
	byte[] myReadBuffer;
	int bufferLength;
	
	public ReaderThread(DeDFS dfs, DFileID dfile, int length) {
		myDFS = dfs;
		myID = dfile;
		bufferLength = length;
	}

	
	public void run() {
		myReadBuffer = new byte[bufferLength];
		myDFS.read(myID, myReadBuffer, 0, bufferLength);
	}
	
	public byte[] getBuffer() {
		return myReadBuffer;
	}
}