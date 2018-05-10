package ubcoin;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
//import org.jgroups.JChannel;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.security.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * 
 * @author sujay
 */
public class UBCoin implements Runnable {

	private static Socket clientSocket = null;
	// The output stream
	private static PrintStream os = null;
	// The input stream
	private static DataInputStream is = null;
	static String clientName=null;
	private static BufferedReader inputLine = null;
	private static boolean closed = false;
    
	public static void main(String[] args) {
		Init.updateBlockchain();
		//Explorer.show();
        int i = 0;
		int portNumber = Config.port;
		// The default host.
		String host = Config.IP;
		
		
		
		
		System.out
				.println("Usage: java MultiThreadUBCoinClient <host> <portNumber>\n"
						+ "Now using host="
						+ host
						+ ", portNumber="
						+ portNumber);

		try {
			clientSocket = new Socket(host, portNumber);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			os = new PrintStream(clientSocket.getOutputStream(),true);
			is = new DataInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to the host "
							+ host);
		}
		if (clientSocket != null && os != null && is != null) {
			try {

				// Create a thread to read from the server. 
				new Thread(new UBCoin()).start();
				// while (!closed) {
				Scanner sc = new Scanner(System.in);
				clientName = sc.nextLine(); 
				send(clientName);
				
				while(true)
				{
					String input = sc.nextLine();
					if(input.equals("start"))
						break;
				}
				send("Joined");
				
				while(true){
				// For transactions 
				File foldertx = new File(Config.path+"/txsend");
				File[] listOfFilestx = foldertx.listFiles();
				
				for (int j = 0; j < listOfFilestx.length; j++) {
					if (listOfFilestx[j].isFile()) {
						String contents=null;
						while(true)
						{
							contents= Reader.readFile(listOfFilestx[j].getAbsolutePath(),0);
							if(contents == null)
								continue;
							else
								break;
							
						}
						listOfFilestx[j].delete();
						
						send(clientName+"!#@"+listOfFilestx[j].getAbsolutePath()+"!#@#tx"+contents);
						contents = null;
					} 
				}
				
				//For Blocks 
				File folderblk = new File(Config.path+"/blksend");
				File[] listOfFilesblk = folderblk.listFiles();

				for (int j = 0; j < listOfFilesblk.length; j++) {
					if (listOfFilesblk[j].isFile()) {
						String contents=null;
						while(true)
						{
						contents= Reader.readFile(listOfFilesblk[j].getAbsolutePath(),1);
						if(!contents.contains("</object-stream>"))
								continue;
							else
								break;
						}
						listOfFilesblk[j].delete();
						System.out.println("Sending announced block");
						send(clientName+"!#@"+listOfFilesblk[j].getAbsolutePath()+"!#@#blk"+contents);
						System.out.println("Sent announced block");
						contents = null;
						
					} 
				} //"there was a comment end here*/
				}
				//END
			} catch (Exception e) {
				System.err.println("IOException:  " + e);
			}
		}
		

	}

	public static void send(String msg) {
		if (!closed) {
			
				
					os.println(msg);
				
			
			
			
		}
	}

	@SuppressWarnings("deprecation")
	public void run() {
		/*
		 * Keep on reading from the socket till we receive "Bye" from the
		 * server. Once we received that then we want to break.
		 */
		String responseLine;
		String fline;
		boolean startAdding = false;
		String testWrite = "";
		int txindex = 0;
		int blkindex = 0;
		int type = 0;
		String path = null;
		long start =0;
		//counter.countPrimes(1000000);
		long end = 0;
		boolean stimer= false;
		
	
		try {
			while ((responseLine = is.readLine()) != null) {
                 

				System.out.println(responseLine);// +" gvar="+glovalVar.gvar+" mindex="+glovalVar.mindex);
				if(responseLine.contains("miner3"))
				{
					System.out.println("Block received from miner3 contains invalid solution");
				}
				if(responseLine.contains("name.Hash"))
				{
					String[] split = responseLine.split("~~");
					Global.id = Integer.parseInt(split[1]);
					Global.counter = Integer.parseInt(split[1]);
					System.out.println("Client id="+Global.id+" Counter="+Global.counter);
				}
				
				if(responseLine.contains("***Hash"))
				{
					String[] split = responseLine.split("~~");
					Global.counter = Integer.parseInt(split[1]);
					System.out.println("Client id="+Global.id+" Counter="+Global.counter);
				}
				
				if (responseLine.contains("<object-stream>")&& !responseLine.contains(clientName)) {
					startAdding = true;
					if (responseLine.contains("#tx")) {
						txindex++;
						type = 0;
						String[] separated = responseLine.split("!#@");
						String[] txname = separated[1].split("tx");
						StringBuilder sb = new StringBuilder(txname[2]);
						System.out.println("File nmae is"+sb);
						path = Config.path+"/txrecv/tx" + separated[0]+sb + "txt";
					}
					if (responseLine.contains("#blk")&& !responseLine.contains(clientName)) {
						blkindex++;
						type = 1;
						
						Writer.writeFile(responseLine, Config.path+"/tempblock/tempblk.txt",1);
						Block b = ReadWrite.readBlk(Config.path+"/tempblock/tempblk.txt");
						String sha = SHA256.applySha256(String.valueOf(b.height)
								+ String.valueOf(b.nonce) + "fafsadg");
						String shasub = sha.substring(0, 4);
						// System.out.println("shasub= "+shasub+" i="+i);
						if (shasub.equals("0000")) {
							File folderblk = new File(Config.path+"/blockchain");
							File[] listOfFilesblk = folderblk.listFiles();
							ReadWrite.writeBlk(b, Config.path+"/blockchain/block"+(listOfFilesblk.length+1)+".txt");
						}
					
						
						path = Config.path+"/blkrecv/blk" + blkindex + "txt";
					}
				}
				if (startAdding)
				{ 
					
					testWrite = testWrite + responseLine;
				}
				if (responseLine.contains("</object-stream>")) {
					startAdding = false;
					// System.out.println("Repeating:" + testWrite);
					if(testWrite !="")
					Writer.writeFile(testWrite, path, type);
					testWrite = "";
					
				}

				if(responseLine.contains("$mine$"))
				{
					String[] minerId = responseLine.split("~");
					if(Integer.parseInt(minerId[1])==Global.id)
					Mine.start_mining(Integer.parseInt(minerId[1]));
					
				}
				if (responseLine.indexOf("*** Bye") != -1)
					break;
			
			
			
			}
			closed = true;
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
}
