package websocketTest;

public class Coin  {
	public String name;
	public String a;
	public String[][] askKRW;
	public String[][] bidKRW;
	public String[][] askBTC;
	public String[][] bidBTC;
	public String parseKRWString;
	public String parseBTCString;
	public Coin(){
		askKRW = new String[10][2];
		bidKRW = new String[10][2];
		askBTC = new String[10][2];
		bidBTC = new String[10][2];
	}
	
	
}
