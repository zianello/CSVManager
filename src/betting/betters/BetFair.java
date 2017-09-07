package betting.betters;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import betting.db.entitites.BetRow;
import betting.utils.NBAStrings;
import betting.utils.PublicStrings;
import betting.utils.PublicStrings.BetType;


public class BetFair {

	private static final String LINK_BASKET = "https://www.betfair.it/sport/basketball";
	private static final String LINK_COD_BASKET_NBA = "?id=5420499&competitionEventId=27219172&action=loadCompetition&modules=multipickavbId@1005&selectedTabType=COMPETITION";
	
	private static final String FILE_PREFIX= "bf_";
	
	private static final String BF_TEAM_HOME_CLASS = "home-team-name";
	private static final String BF_TEAM_AWAY_CLASS = "away-team-name";
	private static final String BF_QUOTES = "ui-runner-price";
	
	public static ArrayList<BetRow> getTodayMatches(BetType bt){
		ArrayList<BetRow> list = new ArrayList<BetRow>();
		switch (bt.getSport()) {
			case CALCIO : list = getTodaySoccerMatches(bt); break;
			case BASKET : list = getTodayBasketMatches(bt); break;
		}
		return list;
	}
	
	private static ArrayList<BetRow> getTodayBasketMatches(BetType bt){
		try {
			  Document doc = Jsoup.connect(getLeagueLink(bt)).get();  
			  
			  Elements away = doc.getElementsByClass(BF_TEAM_HOME_CLASS);
			  Elements home = doc.getElementsByClass(BF_TEAM_AWAY_CLASS);
			  
			  Elements quotes = doc.getElementsByClass(BF_QUOTES);
			 
			  ArrayList<BetRow> list = new ArrayList<BetRow>();
			  
			  for(int i = 0; i < home.size(); i++){
				  
			  String casa = NBAStrings.removeNbaNicknames(home.get(i).text());
			  String trasf = NBAStrings.removeNbaNicknames(away.get(i).text());
			  
			  System.out.print(casa+"   vs   ");
			  System.out.println(trasf);
			  
			  int y = 2*i;
			  System.out.print(quotes.get(y+1).text()+ "  ");
			  System.out.println(quotes.get(y).text()+"\n---------------------------------------");
			  
			  BetRow row = new BetRow();
//			  row.setHome(casa);
//			  row.setAway(trasf);
//			  row.setQuota1(Float.parseFloat(quotes.get(y+1).text()));
//			  row.setQuota2(Float.parseFloat(quotes.get(y).text()));
//			  Calendar now = Calendar.getInstance();
//			  DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//			  row.setDate(format.format(now.getTime()));
//			  row.setPeriod(BasketRow.Period.RS);
			  list.add(row);
		}
			  for(int w = 0; w < list.size(); w++) {
				  list.get(w).printBet();
			  }
		
			  return list;
			  
		    } catch (IOException ex) {
		    	return null;
		    }
		
		
	}
	
	private static ArrayList<BetRow> getTodaySoccerMatches(BetType bt){
		return null;
	}
	
	public static String getFilePath(BetType bt){
		String link = PublicStrings.getPathFile();
		link = link.concat(PublicStrings.getPathFolder(bt.getSport()));
		link = link.concat(FILE_PREFIX);
		link = link.concat(String.valueOf(bt.getLeague()));
		link = link.concat(PublicStrings.getXMLtype());
		return link;
	}
	
	public static String getLeagueLink(BetType bt){
		String link = "";
		switch(bt.getSport()){
		case BASKET : 
			link = LINK_BASKET;
			if(bt.getLeague().equals(PublicStrings.leagues.NBA))
				link = link.concat(LINK_COD_BASKET_NBA);
		default :
		}
		return link;
	}
	
}
