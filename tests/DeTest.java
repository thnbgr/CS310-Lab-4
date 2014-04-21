package tests;

import common.*;
import dblockcache.*;
import dfs.*;
import virtualdisk.*;

public class DeTest {
	public static void main(String[] args) throws InterruptedException {
		DeDFS myDFS = new DeDFS();
		DFileID file1 = myDFS.createDFile();
		//int file1ID = file1.getDFileID();
		byte[] myWriteBuffer = new byte[2];
		byte[] myReadBuffer = new byte[2];
		myWriteBuffer[0] = 'a';
		myWriteBuffer[1] = 'b';
		myDFS.write(file1, myWriteBuffer, 0, 2);
		myDFS.read(file1, myReadBuffer, 0, 2);
		System.out.println(myReadBuffer);
	}
}