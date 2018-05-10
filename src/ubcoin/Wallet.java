/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubcoin;
import java.security.*;
import java.security.spec.*;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author sujay
 */
public class Wallet {
    
    public PrivateKey privateKey;
	public PublicKey publicKey;
    
    public Wallet(){
		generateKeyPair();	
	}
	
    
    public static void main(String ar[])
    {
    	Init.display();
    	Explorer.show();
       ArrayList<Wallet>alread = ReadWrite.readWallet(Config.path+"/wallets/keys.txt");
       ArrayList<Transaction> al = new ArrayList<Transaction>();
       int sender, recv, val,optn,blk,tx;
    	Scanner sc = new Scanner(System.in);
    	while(true)
    	{
    	System.out.println("Do you want to create transaction?");
    	optn = sc.nextInt();
    	if(optn == 0)
    		break;
    	System.out.println("Enter Sender number");
    	sender = sc.nextInt();
    	System.out.println("Enter Receiver number");
    	recv = sc.nextInt();
    	System.out.println("Enter Value");
    	val = sc.nextInt();
    	System.out.println("Enter block number");
    	blk = sc.nextInt();
    	System.out.println("Enter transaction number");
    	tx = sc.nextInt();
    	Transaction t = Transaction.createTx(new int[]{blk},new int[]{tx},alread.get(sender),alread.get(recv),val); 
    	//Transaction t2 = Transaction.createTx(new int[]{blk},new int[]{tx},alread.get(sender),alread.get(recv),val);
    	al.add(t);
    	//al.add(t2);
    	if(t.output.extraData.contains("successful") && Explorer.getBalance(blk, tx)!=val)
    	{
    		Transaction tr = Transaction.createTx(new int[]{-1},new int[]{-1},alread.get(sender),alread.get(sender),Explorer.getBalance(blk, tx)-val);
    		al.add(tr);
    	}
    	}
    	int index=0;
    	for(Transaction t : al)
    	{
    		index++;
    	ReadWrite.writeTx(t,Config.path+"/txsend/tx"+index+".txt" );
    	}
    }
    
    
	public void generateKeyPair() {
		try {
                    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        	privateKey = keyPair.getPrivate();
	        	publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static ArrayList<Transaction> getTransaction(Wallet sender, Wallet receiver, int val) {
		// TODO Auto-generated method stub
		ArrayList<Block> blocks= Blockchain.readBlocks();
		ArrayList<Transaction> al= new ArrayList<Transaction>();
        ArrayList<Integer> pblocks = new ArrayList<Integer>();
        ArrayList<Integer> ptrans = new ArrayList<Integer>();
        int balance = 0;
        int bcount=-1;
        int tcount=-1;
        if(Explorer.getBalance(sender.publicKey.hashCode())<val)//if balance is low, return
        	return null;
        for(Block b:blocks)
         {   
        	 if(balance>=val)break;
             //System.out.println();
             //System.out.println("Block Number="+temp);
             bcount++;
             tcount=-1;
             if(b.tx==null)break;
             for(Transaction t:b.tx)
             {	
            	 tcount++;
            	 if(t.output.receiverKey == sender.publicKey && t.output.spent !=true)
            	 {
            		 balance = balance+ t.output.val;
            		 //t.output.spent=true;
            		 pblocks.add(bcount);
            		 ptrans.add(tcount);
            		 if(balance>=val)break;
            	 }
            	 
             }
         
         }
         
         int[]prevb = new int[pblocks.size()];
         int[]prevt = new int[ptrans.size()];
         
         for(int k=0;k<pblocks.size();k++)
         {
        	 prevb[k]= pblocks.get(k);
        	 prevt[k]= ptrans.get(k);
         }
         
         al.add(Transaction.createTx(prevb,prevt,sender,receiver,val));
         if(balance>val)
         {
        	 al.add(Transaction.createTx(new int[]{-1},new int[]{-1},sender,sender,balance-val));
         }
     return al;
	}



}
