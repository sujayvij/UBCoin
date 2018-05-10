package ubcoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class Writer {

	/**
	 * @param args
	 */
	public static boolean writeTx(String path, Transaction tx) {
		// TODO Auto-generated method stub

		int i = 0;
		try {

			File file = new File(Config.path + "/locks/txlock.txt");
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			FileLock lock = channel.lock();
			if (lock == null) {
				file = null;
				channel = null;
				lock = null;
				return false;
			}
			tx.output.val = i;
			ReadWrite.writeTx(tx, path);
			i++;

			lock.release();
			channel.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block

		} catch (OverlappingFileLockException e) {

			System.out.println("Overlapping File Lock Error: ");
			return false;
		}
		return true;
	}

	public static boolean writeFile(String text, String path, int type) {
		PrintWriter out = null;
		try {

			out = new PrintWriter(path);
			out.println(text);
			out.close();
			// lock.release();
			// channel.close();
		} catch (OverlappingFileLockException e) {

			System.out.println("Overlapping File Lock Error: ");
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}

}
