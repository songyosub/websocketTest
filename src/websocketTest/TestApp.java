package websocketTest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TestApp {
	static String result = "";
	static int CHECKNUMBER = CoinList.getCoinList().length;
	static Coin[] coins = new Coin[CoinList.getCoinList().length];
	static Coin bitcoin = new Coin();
	static AnalyzePremium ap = new AnalyzePremium();
	
	public static void main(String[] args) throws ParseException, InterruptedException {
		String[] coinList = CoinList.getCoinList();
		System.out.println(CoinList.getCoinList().length);
		for(int i = 0; i < coinList.length; i++){
			coins[i] = new Coin();
			coins[i].name = coinList[i];
		}
		bitcoin.name = "BTC";
		
		try {
			// open websocket
			final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(
					new URI("wss://crix-websocket.upbit.com/sockjs/165/mj05ztba/websocket"));

			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					result = message;
//					System.out.println(message);
					if(message.equals("o"))
						return;
					if(message.contains("KRW-BTC")){
						bitcoin.parseKRWString = message;
					}
					for(int i = 0; i < coins.length; i++){
						if(message.contains(coins[i].name)){
							if(message.contains("KRW")){
								coins[i].parseKRWString = message;
								break;
							}else{
								coins[i].parseBTCString = message;
								break;
							}
						}
					}
				}
			});
			while(true){
				// KRW-BTC market request message
				String btcMessage = setMessageString("KRW","BTC");
				clientEndPoint.sendMessage(btcMessage);
				
				// KRW market request message			
				for(int i = 0; i < coinList.length; i++){
					String message = setMessageString("KRW", coinList[i]);
					clientEndPoint.sendMessage(message);
				}
				
				
				// BTC market request message
				for(int i = 0; i < coinList.length; i++){
					String message = setMessageString("BTC", coinList[i]);
					clientEndPoint.sendMessage(message);
				}
				
				Thread.sleep(1000);
				parseResult(bitcoin, 0);
				
				for(int i = 0; i < CoinList.getCoinList().length; i++){
	//				System.out.println(coins[i].name);
					for(int j = 0; j < 2; j++)
						parseResult(coins[i], j);
				}
				
				// set coin and bitcoin ask/bid info
				ap.setCoins(coins);
				ap.setBitcoin(bitcoin);
				ap.analyze();
			}
		} catch (URISyntaxException ex) {
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}
		
	}
	
	public static void parseResult(Coin coin, int index) throws ParseException{
		String parseString;
		boolean isKRW;
		if(index == 0){
			parseString = coin.parseKRWString;
			isKRW = true;
		}else{
			parseString = coin.parseBTCString;
			isKRW = false;
		}
		parseString = parseString.substring(1, parseString.length());
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray)parser.parse(parseString);
		JSONObject jsonObject = (JSONObject) parser.parse((String) jsonArray.get(0));
		String code = (String) jsonObject.get("code");
//		System.out.println("Code : " + code);
		JSONArray orderbookUnits = (JSONArray)jsonObject.get("orderbookUnits");
		for(int i = 0; i < orderbookUnits.size(); i++){
			JSONObject temp = (JSONObject) orderbookUnits.get(i);
			// KRW market ask, bid array
			if (isKRW) {
				coin.askKRW[i][0] = String.format("%.2f", temp.get("askPrice"));
				coin.askKRW[i][1] = String.format("%.2f", temp.get("askSize"));
				coin.bidKRW[i][0] = String.format("%.2f", temp.get("bidPrice"));
				coin.bidKRW[i][1] = String.format("%.2f", temp.get("bidSize"));
			} else {
				// BTC market ask, bid array
				coin.askBTC[i][0] = String.format("%.8f", temp.get("askPrice"));
				coin.askBTC[i][1] = String.valueOf(temp.get("askSize"));
				coin.bidBTC[i][0] = String.format("%.8f", temp.get("bidPrice"));
				coin.bidBTC[i][1] = String.valueOf(temp.get("bidSize"));
			}			
		}
		/*
		System.out.println("ask price");
		for(int i = 0; i < CHECKNUMBER; i++){
			if(isKRW)
				System.out.println(coin.askKRW[CHECKNUMBER-1-i][0] + " / " + coin.askKRW[CHECKNUMBER-1-i][1]);
			else
				System.out.println(coin.askBTC[CHECKNUMBER-1-i][0] + " / " + coin.askBTC[CHECKNUMBER-1-i][1]);
		}
		System.out.println("bid price");
		for(int i = 0; i < CHECKNUMBER; i++){
			if(isKRW)
				System.out.println(coin.bidKRW[CHECKNUMBER-1-i][0] + " / " + coin.bidKRW[CHECKNUMBER-1-i][1]);
			else
				System.out.println(coin.bidBTC[CHECKNUMBER-1-i][0] + " / " + coin.bidBTC[CHECKNUMBER-1-i][1]);
		}
		*/
	}
	
	public static String setMessageString(String coin1, String coin2){
		String messageStr = "[\"[{\\\"ticket\\\":\\\"ram macbook\\\"},{\\\"type\\\":\\\"crixOrderbook\\\",\\\"codes\\\":[\\\"CRIX.UPBIT."+coin1+"-"+coin2+"\\\"]}]\"]";
		return messageStr;
	}
}