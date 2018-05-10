/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubcoin;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author sujay
 */
public class Block {
	int blkId;
	String prevHash;
	long nonce;
	int height;
	int difficulty;
	// Transaction tx;
	ArrayList<Transaction> tx;

	public Block(int blkId, String prevHash, long nonce, int height,
			int difficulty, ArrayList<Transaction> tx) {
		this.blkId = blkId;
		this.prevHash = prevHash;
		this.nonce = nonce;
		this.height = height;
		this.difficulty = difficulty;
		this.tx = tx;
	}

	public Block(Block b) {
		this.blkId = b.blkId;
		this.prevHash = b.prevHash;
		this.nonce = b.nonce;
		this.height = b.height;
		this.difficulty = b.difficulty;
		this.tx = b.tx;
	}

}
