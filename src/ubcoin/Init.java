/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubcoin;

import java.io.File;
import java.util.*;

/**
 * 
 * @author sujay
 */

public class Init {
	public static ArrayList<Transaction> pool = null;
	public static ArrayList<Transaction> al = new ArrayList<>();
	public static ArrayList<Transaction> al2 = new ArrayList<>();
	public static ArrayList<Transaction> al3 = new ArrayList<>();

	public static void updateBlockchain() {
		Blockchain.blockchain.clear();
		File folderblk = new File(Config.path + "/blockchain");
		File[] listOfFilesblk = folderblk.listFiles();

		for (int j = 0; j < listOfFilesblk.length; j++) {
			if (listOfFilesblk[j].isFile()) {
				Block b = ReadWrite.readBlk(
						listOfFilesblk[j].getAbsolutePath(), false);
				Blockchain.blockchain.add(b);
			}
		}
	}

	public static void display() {
		File foldertx = new File(Config.path + "/blockchain");
		File[] listOfFilestx = foldertx.listFiles();

		for (int j = 0; j < listOfFilestx.length; j++) {
			if (listOfFilestx[j].isFile()) {
				Block b = ReadWrite.readBlk(listOfFilestx[j].getAbsolutePath(),
						false);
				Blockchain.addToBlockchain(b);
			}
		}

	}

}
