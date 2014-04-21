package tests;

import common.*;
import dblockcache.*;
import dfs.*;
import virtualdisk.*;

public class DeTest {
	public static void main(String[] args) throws InterruptedException {
		DeDFS myDFS = new DeDFS();
		myDFS.init();
		DFileID file1 = myDFS.createDFile();
		//int file1ID = file1.getDFileID();
		byte[] myWriteBuffer = new byte[2];
		byte[] myReadBuffer = new byte[4];
		myWriteBuffer[0] = 'a';
		myWriteBuffer[1] = 'b';
		System.out.println("Writing...");
		myDFS.write(file1, myWriteBuffer, 0, 2);
		System.out.println("Reading...");
		myDFS.read(file1, myReadBuffer, 0, 2);
<<<<<<< HEAD
		System.out.println(myReadBuffer);
		System.out.println(myWriteBuffer);

=======
		System.out.println(fromByteArray(myReadBuffer));
	}
	public static int fromByteArray(byte[] bytes) {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
>>>>>>> 4b23a22ded55817eb6259c3898379dabd1cc4339
	}
}
