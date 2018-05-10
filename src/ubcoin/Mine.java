package ubcoin;

import java.io.File;
import java.util.ArrayList;

class Mine {

	public static void clearTempFiles(String folder)
	{
		File foldertx = new File(Config.path+"/"+folder);
		File[] listOfFilestx = foldertx.listFiles();
		
		for (int j = 0; j < listOfFilestx.length; j++) {
			if (listOfFilestx[j].isFile()) {
				listOfFilestx[j].delete();
			}
		}
		
	}
	
	
	public static Block startAgain() {
		Block b = null;
		
		ArrayList<Transaction> al = new ArrayList<Transaction>();
		File foldertxp = new File(Config.path+"/txrecv");
		File[] listOfFilestxp = foldertxp.listFiles();
		if(listOfFilestxp.length==0)
			return null;
		else
		{
			System.out.println("Calculation is about to begin");
			long start = System.currentTimeMillis();
			while(true)
			{
				long end = System.currentTimeMillis();
				if(end-start > 1000)
					break;
			}
		
		}
		
		File foldertx = new File(Config.path+"/txrecv");
		File[] listOfFilestx = foldertx.listFiles();
		
		for (int j = 0; j < listOfFilestx.length; j++) {
			if (listOfFilestx[j].isFile()) {
				//String contents = null;
				while (true) {
					Transaction t = ReadWrite.readTx(listOfFilestx[j]
							.getAbsolutePath());
					//if (contents == null)
						//continue;
					if(t!=null) {
						
						
						al.add(t);
						listOfFilestx[j].delete();
						break;
					
					}
				}
			}

		}
		if (al != null) {
			b = new Block(1679, "", 0, Blockchain.blockchain.size(), 0, al);
		}
		return b;
	}

	
	
	public static void start_mining(int miner) {
		System.out.println("Mining started");
		Init.updateBlockchain();
		boolean fallback=false;
		while (true) {
			//System.out.println("Calling start again");
			Block b = startAgain();
			
			if(b == null)continue;
			
			for (long i = 0; i < 1000000000; i++) {
				
				//First check if block is announced
				if(miner!=Global.id || fallback)
				{
				File folderblk = new File(Config.path+"/blkrecv");
				File[] listOfFilesblk = folderblk.listFiles();
				
				if (listOfFilesblk.length > 0) {
					
					Block brecv = ReadWrite.readBlk(listOfFilesblk[0].getAbsolutePath());
					{
						String sha = SHA256.applySha256(String.valueOf(brecv.height)
								+ String.valueOf(brecv.nonce) + "fafsadg");
						String shasub = sha.substring(0, 4);
						// System.out.println("shasub= "+shasub+" i="+i);
						if (shasub.equals("0000")) {
							
							Blockchain.addToBlockchain(brecv);
							for (int j = 0; j < listOfFilesblk.length; j++)
								listOfFilesblk[j].delete();
							break;
						}
						else{System.out.println("Received block contains wrong solution");fallback=true;i=0;System.out.println("So I am mining now");}
					}
					
				}
				}
				//End of announced block checking
				String sha = SHA256.applySha256(String.valueOf(b.height)
						+ String.valueOf(b.nonce) + "fafsadg");
				String shasub = sha.substring(0, 4);
				
				if (shasub.equals("0000")){ 
			
					if(miner==Global.id || fallback)
					{
						
						fallback=false;
						System.out.println("I am "+Global.id+" and I mined and announced block");
						Wallet miner_wallet = new Wallet();
						Transaction t = Transaction.createTx(new int[]{-1},new int[]{-1},miner_wallet,miner_wallet,50);
						t.output.extraData = "coinbase";
						ArrayList<Transaction> alt = new ArrayList<Transaction>();
						alt=b.tx;
						alt.add(0,t);
						b.tx = alt;
						
						Blockchain.addToBlockchain(b);
					ReadWrite.writeBlk(b, Config.path+"/blksend/minedblock.txt");
					return;
					
						}
					else
						b.nonce = 0;
					}
				b.nonce++;
			}
		}
	}
}
