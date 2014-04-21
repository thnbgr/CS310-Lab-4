package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import common.DFileID;
import dfs.*;

public class DeleterThread extends Thread {
	DeDFS myDFS;
	DFileID myID;
	
	public DeleterThread(DeDFS dfs, DFileID dfile) {
		myDFS = dfs;
		myID = dfile;
	}
	
	public void run() {
		myDFS.destroyDFile(myID);
	}
	
	public DFileID getFileID() {
		return myID;
	}
}