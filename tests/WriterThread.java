package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import common.DFileID;
import dfs.*;

public class WriterThread extends Thread {
	DeDFS myDFS;
	DFileID myID;
	String myText;
	byte[] myWriteBuffer;
	
	public WriterThread(DeDFS dfs, DFileID id, String text) {
		myDFS = dfs;
		myID = id;
		myText = text;
		//System.out.println(myText);
	}
	
	public WriterThread(DeDFS dfs, DFileID id, File file) throws FileNotFoundException {
		myDFS = dfs;
		myID = id;
		Scanner myScanner = new Scanner(file);
		myText = "";
		while (myScanner.hasNext()) {
			String nextLine = myScanner.next();
			myText += nextLine+" ";	
		}
		//System.out.println(myText);
	}
	
	public void run() {
		myWriteBuffer = myText.getBytes();
		myDFS.write(myID, myWriteBuffer, 0, myWriteBuffer.length);
	}
	
	public DFileID getFileID() {
		return myID;
	}
	
	public byte[] getBuffer() {
		return myWriteBuffer;
	}
}