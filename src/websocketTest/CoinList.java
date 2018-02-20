package websocketTest;

public class CoinList {
	public static int krwCoinCount = 35;
	public static int btcCoinCount = 34;
	
	public static String[] coinList = {"ADA","NEO","SNT","ETH",
			"BCC","XRP","QTUM","XLM","STEEM","BTG","MER",
			"EMC2","LSK","POWR","ETC","SBD","ARDR","XEM","STORJ",
			"TIX","LTC","WAVES","OMG","STRAT","PIVX","GRS","KMD",
			"REP","XMR","ZEC","DASH","ARK","VTC"};
	
//	public static String[] coinList = {"ETH","ETC","QTUM"};
	
//	public static String[] coinList = {"ETH","QTUM"};
	public static String coin(int index, int cnt) {
		String result = "CRIX.UPBIT.";
		
		// KRW Market
		if(index == 0) {
			result += "KRW-";
		}
		// BTC Market
		else if(index == 1) {
			result += "BTC-";
		}
		result += coinList[cnt-1];
		return result;
	}
	public static String[] getCoinList() {
		return coinList;
	}
}
