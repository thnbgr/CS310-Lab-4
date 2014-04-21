package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import common.*;
import dblockcache.*;
import dfs.*;
import virtualdisk.*;

public class DeTestWWThreads {
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		// Initialize DFS
		DeDFS myDFS = new DeDFS();
		myDFS.init();
		// Create DFileIDs
		DFileID fileName1 = myDFS.createDFile();

		String filePath1 = "test2.txt";
		String filePath2 = "test3.txt";
		File inputFile1 = new File(filePath1);
		File inputFile2 = new File(filePath2);
		WriterThread writer1 = new WriterThread(myDFS, fileName1, inputFile1);
		WriterThread writer2 = new WriterThread(myDFS, fileName1, inputFile2);
		DFileID fileID1 = writer1.getFileID();
		ReaderThread reader1 = new ReaderThread(myDFS, fileID1, 50000);
		writer1.start();
		Thread.sleep(1000);
		writer2.start();
		reader1.start();
		}
	}

