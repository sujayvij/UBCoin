package ubcoin;

import java.io.File;

class ClearCache {

	public static void clearTempFiles(String folder) {
		File foldertx = new File(Config.path + "/" + folder);
		File[] listOfFilestx = foldertx.listFiles();

		for (int j = 0; j < listOfFilestx.length; j++) {
			if (listOfFilestx[j].isFile()) {
				listOfFilestx[j].delete();
			}
		}

	}

	public static void clear() {

		clearTempFiles("blkrecv");
		clearTempFiles("blksend");
		clearTempFiles("tempblock");
		clearTempFiles("txrecv");
		clearTempFiles("txsend");

	}

}