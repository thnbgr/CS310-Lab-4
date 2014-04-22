package common;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class LogWriter {
	
	private static String file_path = "src/DeFiler.log";

	public static void log(String str, long time) {
	
		
	    PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(
					(new BufferedWriter(new FileWriter(file_path, true)))
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    printWriter.println(time+": "+str);
	    printWriter.close();
	}
	
	public static void flush() {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(
					(new BufferedWriter(new FileWriter(file_path, false)))
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    printWriter.print("");
	    printWriter.close();
	}
}
package common;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class LogWriter {
	
	private static String file_path = "src/DeFiler.log";

	public static void log(String str, long time) {
	
		
	    PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(
					(new BufferedWriter(new FileWriter(file_path, true)))
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    printWriter.println(time+": "+str);
	    printWriter.close();
	}
	
	public static void flush() {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(
					(new BufferedWriter(new FileWriter(file_path, false)))
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    printWriter.print("");
	    printWriter.close();
	}
}
