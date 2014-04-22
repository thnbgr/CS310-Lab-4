package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class CrashCheck {
	int recentFileNumber;
	boolean crashed;
	public CrashCheck(File fileName) {
		Scanner myScanner;
		try {
			myScanner = new Scanner(fileName);

			crashed = false;
			boolean writeStart = false;
			
			while (myScanner.hasNext()) {
				String nextLine = myScanner.next();
				if (nextLine.equals("User")) {
					if (writeStart) {
						crashed = true;
						break;
					}
					writeStart = true;
					myScanner.next();
					myScanner.next();
					myScanner.next();
					String temp = myScanner.next();
					recentFileNumber = Integer.parseInt(temp.substring(1,temp.length()));
				}
				if (nextLine.equals("Commited")) {
					writeStart = false;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean didCrash() {
		return crashed;
	}
	
	public int crashedFile() {
		if (crashed) {
			return recentFileNumber;
		}
		else {
			return -1;
		}
	}

}