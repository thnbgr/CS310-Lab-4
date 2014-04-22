package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import common.DFileID;
import dfs.*;

public class ReaderThread extends Thread {
	DeDFS myDFS;
	DFileID myID;
	byte[] myReadBuffer;
	int bufferLength;
	String myFileName;
	
	public ReaderThread(DeDFS dfs, DFileID dfile, int length, String fileName) {
		myDFS = dfs;
		myID = dfile;
		bufferLength = length;
		myFileName = fileName;
	}

	
	public void run() {
		myReadBuffer = new byte[bufferLength];
		myDFS.read(myID, myReadBuffer, 0, bufferLength);
		try {
			FileWriter fstream = new FileWriter(myFileName + ".txt");
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write(new String(myReadBuffer));
	        // Close the output stream
	        out.close();
		} catch (Exception ex) {// Catch exception if any
            System.err.println("Error: " + ex.getMessage());
        }
	}
	
	public byte[] getBuffer() {
		return myReadBuffer;
	}
}