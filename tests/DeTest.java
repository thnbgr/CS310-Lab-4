package tests;

import java.util.Arrays;

import common.*;
import dblockcache.*;
import dfs.*;
import virtualdisk.*;

public class DeTest {
	public static void main(String[] args) throws InterruptedException {
		DeDFS myDFS = new DeDFS();
		myDFS.init();
		DFileID file1 = myDFS.createDFile();
		String contents = "";
		for (int i = 0; i < 10000-1; i++) {
			contents += (char) i;
		}
		byte[] in = contents.getBytes();
		byte[] out = new byte[in.length];
		System.out.println("Writing...");
		myDFS.write(file1, in, 0, in.length);
		System.out.println("Reading...");
		myDFS.read(file1, out, 0, out.length);
		String s = new String(out);
		System.out.println(s.charAt(9000));
	}
}
