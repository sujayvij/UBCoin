package ubcoin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

public class ReadWrite {

	/**
	 * @param args
	 */

	public static void writeWallet(ArrayList<Wallet> w, String path) {
		XStream xstream = new XStream();
		ObjectOutputStream out = null;

		try {

			// String path = "F:/ubtx/tx"+txid+".txt";
			out = xstream.createObjectOutputStream(new FileOutputStream(path,
					true));
			out.writeObject(w);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<Wallet> readWallet(String path) {
		// TODO Auto-generated method stub

		ArrayList<Wallet> w = null;
		try {
			File f = new File(path);
			if (f.exists()) {

				XStream xstream = new XStream();

				ObjectInputStream in = xstream
						.createObjectInputStream(new FileInputStream(path));

				w = (ArrayList<Wallet>) in.readObject();

				in.close();

				// f.delete();
			}

		} catch (Exception e) {
		}
		return w;
	}

	public static void writeTx(Transaction t, String path) {
		XStream xstream = new XStream();
		ObjectOutputStream out = null;

		try {

			// String path = "F:/ubtx/tx"+txid+".txt";
			out = xstream.createObjectOutputStream(new FileOutputStream(path,
					true));
			out.writeObject(t);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeBlk(Block b, String path) {
		XStream xstream = new XStream();
		ObjectOutputStream out = null;

		try {

			// String path = "F:/ubtx/tx"+txid+".txt";
			out = xstream.createObjectOutputStream(new FileOutputStream(path,
					true));
			out.writeObject(b);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Transaction readTx(String path) {
		// TODO Auto-generated method stub
		// String path = "F:/ubtx/tx"+txid+".txt";
		// String path = "F:/ubtxwrite/filename.txt";
		Transaction t = null;
		try {
			File f = new File(path);
			if (f.exists()) {

				XStream xstream = new XStream();

				ObjectInputStream in = xstream
						.createObjectInputStream(new FileInputStream(path));

				t = (Transaction) in.readObject();

				in.close();

				f.delete();
			}

		} catch (Exception e) {
		}
		return t;
	}

	public static Block readBlk(String path, boolean delete) {
		// TODO Auto-generated method stub
		// String path = "F:/ubtx/tx"+txid+".txt";
		// String path = "F:/ubtxwrite/filename.txt";
		Block b = null;
		try {
			File f = new File(path);
			if (f.exists()) {

				XStream xstream = new XStream();

				ObjectInputStream in = xstream
						.createObjectInputStream(new FileInputStream(path));

				b = (Block) in.readObject();

				in.close();

			}

		} catch (Exception e) {
		}
		return b;
	}

	public static Block readBlk(String path) {
		// TODO Auto-generated method stub
		// String path = "F:/ubtx/tx"+txid+".txt";
		// String path = "F:/ubtxwrite/filename.txt";
		Block b = null;
		try {
			File f = new File(path);
			if (f.exists()) {
				File file_lock = new File(Config.path + "/locks/blklock.txt");
				FileChannel channel = new RandomAccessFile(file_lock, "rw")
						.getChannel();
				FileLock lock = channel.tryLock();
				if (lock == null)
					return null;

				XStream xstream = new XStream();

				ObjectInputStream in = xstream
						.createObjectInputStream(new FileInputStream(path));

				b = (Block) in.readObject();

				in.close();

				lock.release();
				channel.close();

				f.delete();
			}

		} catch (Exception e) {
		}
		return b;
	}

	public static void writeFile(String text, String path) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(text);
		out.close();
	}

	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		String contents = "";
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				// stringBuilder.append(line);
				// stringBuilder.append(ls);
				contents += line;
			}

			// return stringBuilder.toString();
			return contents;
		} finally {
			reader.close();
		}
	}

}
