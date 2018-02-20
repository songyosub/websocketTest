package websocketTest;

public class AnalyzePremium {
	public static String BTCPrice;
	public Coin[] coins;
	public Coin bitcoin;
	public static long MAXINVESTKRW = 1000000;
	public int called = 0;
	
	public static String getBTCPrice() {
		return BTCPrice;
	}
	public static void setBTCPrice(String bTCPrice) {
		BTCPrice = bTCPrice;
	}
	public Coin[] getCoins() {
		return coins;
	}
	public void setCoins(Coin[] coins) {
		this.coins = coins;
	}
	public Coin getBitcoin() {
		return bitcoin;
	}
	public void setBitcoin(Coin bitcoin) {
		this.bitcoin = bitcoin;
	}
	
	public void analyze(){
		long btcKRWask = (long)Double.parseDouble(bitcoin.askKRW[0][0]);
		double btcKRWaskCount = Double.parseDouble(bitcoin.askKRW[0][1]);
		long btcKRWbid = (long)Double.parseDouble(bitcoin.bidKRW[0][0]);
		double btcKRWbidCount = Double.parseDouble(bitcoin.bidKRW[0][1]);
		//System.out.println("BTC KRW ask : " + btcKRWask + " / " + btcKRWaskCount);
		//System.out.println("BTC KRW bid : " + btcKRWbid + " / " + btcKRWbidCount);
		System.out.print(".");
		called++;
		if(called % 30 == 0)
			System.out.println();
		for(int i = 0; i < coins.length; i++){
			Coin test = coins[i];
			long coinKRWask = (long)Double.parseDouble(test.askKRW[0][0]);
			double coinKRWaskCount = Double.parseDouble(test.askKRW[0][1]);
			long coinKRWbid = (long)Double.parseDouble(test.bidKRW[0][0]);
			double coinKRWbidCount = Double.parseDouble(test.bidKRW[0][1]);
			double coinBTCask = Double.parseDouble(test.askBTC[0][0]);
			double coinBTCaskCount = Double.parseDouble(test.askBTC[0][1]);
			double coinBTCbid = Double.parseDouble(test.bidBTC[0][0]);
			double coinBTCbidCount = Double.parseDouble(test.bidBTC[0][1]);
//			System.out.println(test.name + " KRW ask : " + coinKRWask + " / " + coinKRWaskCount);
//			System.out.println(test.name + " KRW bid : " + coinKRWbid + " / " + coinKRWbidCount);
//			System.out.println(test.name + " BTC ask : " + (long)(coinBTCask * btcKRWask) + " / " + coinBTCaskCount + " / " + coinBTCask);
//			System.out.println(test.name + " BTC bid : " + (long)(coinBTCbid * btcKRWbid) + " / " + coinBTCbidCount + " / " + coinBTCbid);
			int index = 0;
			// scenario 1 
			// KRW market ask vs BTC market bid + KRW/BTC bid
			// suppose KRW market ask is cheaper than BTC market bid
			// scenario 2
			// KRW market bid vs BTC market ask + KRW/BTC ask
			// suppose BTC market ask is cheaper than KRW market bid
			double s1 = (((long)(coinBTCbid * btcKRWbid) - coinKRWask)/(double)coinKRWask)*100;
			double s2 = ((coinKRWbid - (long)(coinBTCask * btcKRWask))/(double)(coinBTCask * btcKRWask))*100;
			if(s1 > 0.8|| s2 > 0.8){
				index++;
			}
			
			
			if(index > 0){
				System.out.print(test.name+" ");
				if(s1 > 0){
					System.out.printf("%.2f ",s1);
				}
				if(s2 > 0){
					System.out.printf("%.2f ",s2);
				}
			}
			
		}
	}
	
}
