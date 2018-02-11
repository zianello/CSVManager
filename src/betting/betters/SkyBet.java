package betting.betters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import betting.db.entitites.BetRow;
import betting.exceptions.NoOddsException;
import betting.utils.PublicStrings;
import betting.utils.Utils;
import betting.utils.PublicStrings.BetType;
import betting.utils.PublicStrings.bet_types;
import betting.utils.PublicStrings.betters;
import betting.utils.PublicStrings.leagues;
import betting.utils.PublicStrings.sports;

public class SkyBet {
	
	private static final String FILE_PREFIX= "sb_";
	
	private static final String LINK_START = "https://www.skybet.it/";
	private static final String LINK_SOCCER = "calcio/";
	
	private static final Map<BetType, String> leaguesLinkList = createLinksMap(); 
	
	public static ArrayList<BetRow> getTodayMatches(BetType bt) throws NoOddsException, IOException{
		ArrayList<BetRow> list = new ArrayList<BetRow>();
		switch (bt.getSport()) {
			case CALCIO : list = getTodaySoccerMatches(bt); break;
		}
		return list;
	}

	private static ArrayList<BetRow> getTodaySoccerMatches(BetType bt) throws NoOddsException, IOException{
		
	  String source = getLeagueLink(bt);
	  Document doc = Jsoup.connect(source).get();

	  System.out.println("\n" + doc + "\n");
	  return null;
	}

	private static Map<BetType, String> createLinksMap(){
		Map<BetType, String> leagueLinksList = new HashMap<BetType, String>();
		leagueLinksList.put(new BetType(sports.CALCIO, betters.SKYBET, bet_types.ESITO1X2, leagues.A), "344/italia-serie-a");
		return leagueLinksList;
	}
	
			  
	public static String getLeagueLink (BetType bt){
		String link = LINK_START;
		switch (bt.getSport()) {
		case CALCIO : {
			link = link.concat(LINK_SOCCER);
			leaguesLinkList.get(bt);
			link = link.concat(leaguesLinkList.get(bt));
			break;
			}	
		}
		return link;
	}
		
	public static String getFilePath(BetType bt){
		String link = PublicStrings.getPathFile();
		link = link.concat(PublicStrings.getPathFolder(bt.getSport()));
		link = link.concat(FILE_PREFIX);
		link = link.concat(bt.getLeague().toString());
		link = link.concat(PublicStrings.getCSVtype());		//link = link.concat(PublicStrings.getXMLtype());
		return link;
	}
	

}
