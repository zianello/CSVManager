package betting.betters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import betting.db.entitites.BetRow;
import betting.exceptions.NoOddsException;
import betting.utils.PublicStrings;
import betting.utils.PublicStrings.BetType;
import betting.utils.PublicStrings.bet_types;
import betting.utils.PublicStrings.betters;
import betting.utils.PublicStrings.leagues;
import betting.utils.PublicStrings.sports;

public class BetClic {
	
	private static final String FILE_PREFIX= "bc_";
	
	private static final String LINK_START = "https://www.betclic.it/";
	private static final String LINK_SOCCER = "calcio/";
	
	private static final String BC_MATCH_CLASS = "match-name";
	private static final String BC_ODDS_CLASS = "match-odds";
	private static final String BC_TIME_CLASS = "time";
	
	private static final Map<BetType, String> leaguesLinkList = createLinksMap(); 
	
	public static ArrayList<BetRow> getTodayMatches(BetType bt) throws NoOddsException, IOException{
		ArrayList<BetRow> list = new ArrayList<BetRow>();
		switch (bt.getSport()) {
			case CALCIO : list = getTodaySoccerMatches(bt); break;
			default: 
		}
		return list;
	}

	private static ArrayList<BetRow> getTodaySoccerMatches(BetType bt) throws NoOddsException, IOException{
		
		  String source = getLeagueLink(bt);
		  Document doc = Jsoup.connect(source).get();
		
		  Elements dates = doc.getElementsByAttribute("data-date");
		  
		  ArrayList<BetRow> listBets = new ArrayList<BetRow>();
		  ArrayList<String[]> total = new ArrayList<String[]>();	
		  
		  for(int y = 0; y < dates.size(); y++){
		  
			  Elements time = dates.get(y).getElementsByTag(BC_TIME_CLASS);
			  Elements matches = dates.get(y).getElementsByClass(BC_MATCH_CLASS);
			  Elements quotes = dates.get(y).getElementsByClass(BC_ODDS_CLASS);
			  
			  for(int i = 0; i < matches.size(); i++){
				  String[] match = new String[6];
				  String[] m = matches.get(i).getElementsByTag("a").text().split("-");
				  match[0] = time.get(0).text();	//data
				  match[1] = m[0].trim();			//home
				  match[2] = m[1].trim();			//away
				  String[] q = quotes.get(i).text().split(" ");
				  match[3] = q[0].replace(",", ".").trim();			//1
				  match[4] = q[1].replace(",", ".").trim();			//X
				  match[5] = q[2].replace(",", ".").trim();			//2
				  total.add(match);
			  }
		  }
		  
		  for(int w = 0; w < total.size(); w++) {
			  
			  Map<bet_types, ArrayList<String>> betList = PublicStrings.betTypeList;
			  List<String> betTypeList = betList.get(bt.getBet_type());
			  
			  for(int i = 0; i < betTypeList.size(); i++){
				  int y = 3+i;
				  BetRow r = new BetRow(total.get(w)[1],total.get(w)[2],
						  Float.parseFloat(total.get(w)[y]),
						  bt.getBet_type().toString(),
						  total.get(w)[0],
						  bt.getLeague().toString(),
						  bt.getBetter().toString(),
						  betTypeList.get(i),
						  PublicStrings.SIST_PROV,
						  bt.getSport().toString()
						  );
				  listBets.add(r);  
				  r.printBet();
			  }
		  }
		  
		  return listBets;
	}

	private static Map<BetType, String> createLinksMap(){
		Map<BetType, String> leagueLinksList = new HashMap<BetType, String>();
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.A), "serie-a-e6");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.PL), "inghilterra-pr-league-e3");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.LIGA), "spagna-liga-primera-e7");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.BL), "germania-bundesliga-e5");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.L1), "francia-ligue-1-e4");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.ED), "olanda-eredivisie-e21");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.LP), "portogallo-primeira-liga-e32");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.BLG), "belgio-jupiler-league-e26");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.DSP), "danimarca-superliga-e88");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.BETCLIC, bet_types.ESITO1X2, leagues.TSL), "turchia-super-lig-e37");
		
		
		
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
		default:
		}
		return link;
	}
		
	public static String getFilePath(BetType bt){
		String link = PublicStrings.getPathFile();
		link = link.concat(PublicStrings.getPathFolder(bt.getSport()));
		link = link.concat(FILE_PREFIX);
		link = link.concat(bt.getLeague().toString());
		link = link.concat(PublicStrings.getCSVtype());
		return link;
	}

}

