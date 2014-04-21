package tests;

import java.io.File;
import java.io.FileNotFoundException;

import common.*;
import dblockcache.*;
import dfs.*;
import virtualdisk.*;

public class DeTestWDThreads {
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		// Initialize DFS
		DeDFS myDFS = new DeDFS();
		myDFS.init();
		// Create DFileIDs
		DFileID fileName1 = myDFS.createDFile();

		String filePath1 = "test2.txt";
		File inputFile1 = new File(filePath1);
		WriterThread writer1 = new WriterThread(myDFS, fileName1, inputFile1);
		DFileID fileID1 = writer1.getFileID();
		ReaderThread reader1 = new ReaderThread(myDFS, fileID1, 100);
		ReaderThread reader2 = new ReaderThread(myDFS, fileID1, 100);
		writer1.start();
		reader1.start();
		Thread.sleep(1000);
		String output1 = new String(reader1.getBuffer());
		System.out.println("First written is: "+output1);
		Thread.sleep(1000);
		DeleterThread deleter1 = new DeleterThread(myDFS, fileID1);
		deleter1.start();
		reader2.start();
		Thread.sleep(1000);
		String output2 = new String(reader2.getBuffer());
		System.out.println("After deleted is: "+output2);
		
		// Create new DFIleID
		DFileID fileName2 = myDFS.createDFile();
		WriterThread writer2 = new WriterThread(myDFS, fileName2, inputFile1);
		DFileID fileID2 = writer2.getFileID();
		ReaderThread reader3 = new ReaderThread(myDFS, fileID2, 100);
		writer2.start();
		reader3.start();
		Thread.sleep(1000);
		String output3 = new String(reader3.getBuffer());
		System.out.println("After rewrite is: "+output3);
		}
}
