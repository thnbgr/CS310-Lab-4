package tests;

import java.io.File;
import java.io.FileNotFoundException;

import common.*;
import dblockcache.*;
import dfs.*;
import virtualdisk.*;

public class DeTestRWThreads {
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		// Initialize DFS
		DeDFS myDFS = new DeDFS();
		myDFS.init();
		
		String filePath = "test1.txt";
		File inputFile1 = new File(filePath);
		WriterThread writer1 = new WriterThread(myDFS, inputFile1);
		DFileID fileID1 = writer1.getFileID();
		ReaderThread reader1 = new ReaderThread(myDFS, fileID1, 100);
		writer1.start();
		reader1.start();
		String output = new String(reader1.getBuffer());
		System.out.println("Output is: "+output);
	}
}
