package ubcoin;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class Reader {
	public static String readFile(String path, int type) {
		
		String contents = null;
		try {
			
			
			contents = ReadWrite.readFile(path);
			
		} 
		catch (OverlappingFileLockException e) {

			System.out.println("Overlapping File Lock Error: ");
			return null;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	return contents;
	
	}
}
