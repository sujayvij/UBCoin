package ubcoin;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;

public class Explorer {
	public static ArrayList<Wallet> alread = ReadWrite.readWallet(Config.path
			+ "/wallets/keys.txt");

	/**
	 * @param args
	 */

	public static void main(String ar[]) {
		ClearCache.clear();
		ArrayList<Block> blocks = Blockchain.readBlocks();
		int temp = 0;
		int txcount = 0;
		File folderblk = new File(Config.path + "/blockchain");
		File[] listOfFilesblk = folderblk.listFiles();

		for (int j = 0; j < listOfFilesblk.length; j++) {
			if (listOfFilesblk[j].isFile()) {
				Block b = ReadWrite.readBlk(
						listOfFilesblk[j].getAbsolutePath(), false);
				{

					System.out.println();
					System.out.println("Block Number=" + (temp + 1));
					temp++;
					if (b.tx == null)
						break;
					for (Transaction t : b.tx) {

						// for(int j=0;j<t.input.length;j++)
						// System.out.println("Sender="+t.input[j].senderKey.hashCode()+" Receiver="+t.output.receiverKey.hashCode()+" Extra Data="+t.output.extraData);

						for (int k = 0; k < t.input.length; k++) {
							if (txcount != 3)
								System.out.println("Sender="
										+ getWalletString(t.input[k].senderKey)
										+ " Receiver="
										+ getWalletString(t.output.receiverKey)
										+ " Extra Data=" + t.output.extraData);
						}
						txcount++;
					}

				}
			}
		}
	}

	public static String getWalletString(PublicKey pk) {
		ArrayList<Block> blocks = Blockchain.readBlocks();
		int temp = 0;

		for (Wallet w : alread) {

			if (w.publicKey.hashCode() == pk.hashCode())
				return String.valueOf(temp);
			temp++;

		}
		return null;
	}

	public static void show() {

		ArrayList<Block> blocks = Blockchain.readBlocks();
		int temp = 0;
		for (Block b : blocks) {
			System.out.println();
			System.out.println("Block Number=" + temp);
			temp++;
			if (b.tx == null)
				break;
			for (Transaction t : b.tx) {

				// for(int j=0;j<t.input.length;j++)
				// System.out.println("Sender="+t.input[j].senderKey.hashCode()+" Receiver="+t.output.receiverKey.hashCode()+" Extra Data="+t.output.extraData);

				for (int j = 0; j < t.input.length; j++)
					System.out.println("Sender="
							+ getWalletString(t.input[j].senderKey)
							+ " Receiver="
							+ getWalletString(t.output.receiverKey)
							+ " Extra Data=" + t.output.extraData);
			}

		}
	}

	public static int getBalance(int target) {
		// TODO Auto-generated method stub
		ArrayList<Block> blocks = Blockchain.readBlocks();
		int temp = 0;
		int balance = 0;

		for (Block b : blocks) {
			// System.out.println();
			// System.out.println("Block Number="+temp);
			temp++;
			if (b.tx == null)
				break;
			for (Transaction t : b.tx) {
				// System.out.println("Next Transaction");
				if (t.input[0].senderKey == t.output.receiverKey)
					continue;
				if (t.output.success) {
					if (t.input[0].senderKey.hashCode() == target) {
						balance = balance - t.output.val;
					}

					if (t.output.receiverKey.hashCode() == target) {
						balance = balance + t.output.val;
					}
					// +" Receiver="+t.output.receiverKey.hashCode());
				}
				// System.out.println(t);
			}

		}
		return balance;
	}

	public static int getBalance(int blk, int tx) {
		// TODO Auto-generated method stub
		ArrayList<Block> blocks = Blockchain.readBlocks();
		int temp = 0;
		int balance = 0;
		Block b = blocks.get(blk);
		Transaction t = b.tx.get(tx);

		return t.output.val;
	}

}
