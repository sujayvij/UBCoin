/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubcoin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 * 
 * @author sujay
 */
public class Blockchain implements Serializable {
	static ObjectOutputStream os = null;
	static ObjectInputStream ois = null;
	static ArrayList<Block> blockchain = new ArrayList<Block>();

	static public void addToBlockchain(Block b) {

		blockchain.add(b);

		ReadWrite.writeBlk(b,
				Config.path + "/blockchain/block" + blockchain.size() + ".txt");

	}

	static public ArrayList<Block> readBlocks() {

		return blockchain;
	}

}
