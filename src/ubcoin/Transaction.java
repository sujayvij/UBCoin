/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubcoin;

import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import static ubcoin.Blockchain.blockchain;
import static ubcoin.Init.al;
//import static ubcoin.Init.pool;
//import static ubcoin.UBCoin.al;

/**
 * 
 * @author sujay
 */
class input implements Serializable {

	int prevBlk;
	String txid;
	int utxo;
	byte[] sign;
	PublicKey senderKey;

	public input(int prevBlk, String txid, int utxo, byte[] sign,
			PublicKey senderKey) {
		this.prevBlk = prevBlk;
		this.txid = txid;
		this.utxo = utxo;
		this.sign = sign;
		this.senderKey = senderKey;
	}
}

class output implements Serializable {

	int utxo;
	int val;
	PublicKey receiverKey;
	String extraData;
	boolean spent;
	boolean success;

	public output(int utxo, int val, PublicKey receiverKey, String extraData,
			boolean spent, boolean success) {
		this.utxo = utxo;
		this.val = val;
		this.receiverKey = receiverKey;
		this.extraData = extraData;
		this.spent = spent;
		this.success = success;
	}
}

class spentop {

	int prevBlk;
	int prevOp;
	Wallet sender;

	spentop(int prevBlk, int prevOp, Wallet sender) {
		this.prevBlk = prevBlk;
		this.prevOp = prevOp;
		this.sender = sender;
	}
}

public class Transaction implements Serializable {

	public input[] input;
	public output output;
	static ArrayList<spentop> prevTx = new ArrayList<spentop>();
	public String dochash;

	public Transaction(input[] input, output output) {
		this.input = input;
		this.output = output;
	}

	public static Transaction createTx(int[] prevBlk, int[] prev,
			Wallet sender, Wallet receiver, int val) {
		input[] in = new input[prevBlk.length];
		output prevop = null;
		byte[] signature = null;
		// Check for same output referenced twice
		int balance = 0;
		// System.out.println("new val");
		for (int k = 0; k < prevBlk.length; k++) {
			for (spentop op : Transaction.prevTx) {
				if (op.prevBlk == prevBlk[k] && op.prevOp == prev[k]
						&& op.sender == sender) {
					in[k] = new input(-1, "", -1, Sign.apply(sender.privateKey,
							receiver.publicKey.toString()), sender.publicKey);
					output out = new output(-1, 0, receiver.publicKey,
							"Same output referenced twice by same sender",
							false, false);
					Transaction tx = new Transaction(in, out);
					return tx;
				}
			}
		}
		// end of check

		for (int i = 0; i < prevBlk.length; i++) {

			if (prevBlk[i] >= blockchain.size()) {
				in[i] = new input(-1, "", -1, Sign.apply(sender.privateKey,
						receiver.publicKey.toString()), sender.publicKey);
				output out = new output(-1, 0, receiver.publicKey,
						"Block size overflow", false, false);
				Transaction tx = new Transaction(in, out);
				return tx;
			}
			if (prev[i] != -1) {
				if (prevBlk[i] != -1) {
					Block bprev = blockchain.get(prevBlk[i]);

					if (prev[i] >= bprev.tx.size()) {
						in[i] = new input(-1, "", -1, Sign.apply(
								sender.privateKey,
								receiver.publicKey.toString()),
								sender.publicKey);
						output out = new output(-1, 0, receiver.publicKey,
								"Transaction size overflow", false, false);
						Transaction tx = new Transaction(in, out);
						return tx;
					}

					prevop = bprev.tx.get(prev[i]).output;
					if (prevop.spent == true) {
						in[i] = new input(-1, "", -1, Sign.apply(
								sender.privateKey,
								receiver.publicKey.toString()),
								sender.publicKey);
						output out = new output(-1, 0, receiver.publicKey,
								"Output already spent", false, false);
						Transaction tx = new Transaction(in, out);
						return tx;
					}

					signature = Sign.apply(sender.privateKey,
							prevop.receiverKey.toString());
				} else {
					prevop = al.get(prev[i]).output;
					signature = Sign.apply(sender.privateKey,
							prevop.receiverKey.toString());
				}
			}

			System.out.println();
			if (prev[i] == -1
					|| Sign.verify(prevop.receiverKey,
							prevop.receiverKey.toString(), signature)) {
				if (prevop != null) {
					balance = balance + prevop.val;
					prevop.spent = true;
					System.out.println("val=" + prevop.val);
				}

				in[i] = new input(-1, "", -1, Sign.apply(sender.privateKey,
						receiver.publicKey.toString()), sender.publicKey);
				/*
				 * output out = new output(-1,val,
				 * receiver.publicKey,"Transaction successful"); Transaction tx
				 * = new Transaction(in,out); return tx;
				 */
			} else {
				in[i] = new input(-1, "", -1, Sign.apply(sender.privateKey,
						receiver.publicKey.toString()), sender.publicKey);
				output out = new output(-1, 0, receiver.publicKey,
						"Transaction failed", false, false);
				Transaction tx = new Transaction(in, out);
				return tx;
			}
		}

		/**
		 * ***********if sender is verified but balance is low***************
		 */
		if (prev[0] != -1 && balance < val) {
			output out = new output(-1, 0, receiver.publicKey, "Low balance",
					false, false);
			for (int k = 0; k < prevBlk.length; k++) {
				if (prevBlk[k] >= 0 && prev[k] >= 0) {
					if (blockchain.get(prevBlk[k]).tx.get(prev[k]).output != null)
						blockchain.get(prevBlk[k]).tx.get(prev[k]).output.spent = false;
				}
			}
			Transaction tx = new Transaction(in, out);
			return tx;
		}
		/**
		 * *****************************************************************
		 */
		output out = new output(-1, val, receiver.publicKey,
				"Transaction successful: val=" + val, false, true);
		Transaction tx = new Transaction(in, out);

		for (int k = 0; k < prevBlk.length; k++) {

			prevTx.add(new spentop(prevBlk[k], prev[k], sender));
		}

		return tx;
	}

}
