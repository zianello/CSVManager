package betting.betters;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import betting.utils.NBAStrings;
import betting.utils.PublicStrings;
import betting.utils.PublicStrings.BetType;
import betting.utils.PublicStrings.bet_types;
import betting.utils.PublicStrings.betters;
import betting.utils.PublicStrings.leagues;
import betting.utils.PublicStrings.sports;
import betting.utils.Utils;

public class WilliamHill {

	private static final String FILE_PREFIX= "wh_";
	
	private static final String LINK_START = "http://sports.williamhill.it/bet_ita/it/betting/";
	private static final String LINK_END = "Money-Line.html";
	
	private static final String LINK_BASKET = "y/";
	private static final String LINK_SOCCER = "g/";
	
	private static final String LINK_COD_BASKET_NBA = "8371/";
	private static final String LINK_COD_BASKET_SERIEA = "38113/";
	
	//classi XML WHILL
	private static final String WH_QUOTE_CLASS = "eventprice";
	private static final String WH_BASKET_MATCH_CLASS = "CentrePad";
	private static final String WH_SOCCER_MATCH_CLASS = "LeftPad";
	
	private static final Map<BetType, String> leaguesLinkList = createLinksMap(); 
	
	public static ArrayList<BetRow> getTodayMatches(BetType bt) throws NoOddsException{
		ArrayList<BetRow> list = new ArrayList<BetRow>();
		switch (bt.getSport()) {
			case CALCIO : list = getTodaySoccerMatches(bt); break;
			case BASKET : list = getTodayBasketMatches(bt); break;
		}
		return list;
	}
	
	private static ArrayList<BetRow> getTodaySoccerMatches(BetType bt) throws NoOddsException{
		
		try {
			  String source = getLeagueLink(bt);
			  Document doc = Jsoup.connect(source).get();

			  System.out.println("\n" + source + "\n");
			  
			  Elements quotes = doc.getElementsByClass(WH_QUOTE_CLASS);
			  Elements matches = doc.getElementsByClass(WH_SOCCER_MATCH_CLASS);
			  
			  ArrayList<BetRow> listBets = new ArrayList<BetRow>();
			  ArrayList<String[]> total = new ArrayList<String[]>();		  
//			  System.out.println(matches);

			  // j = date, j+1 = hour j+2 = match
			  for(int j= 2; j < matches.size(); j=j+3){
				  
				  String[] match  = new String[3];
				  String dateMatch = matches.get(j).text();
				  dateMatch = Utils.convertMonths(dateMatch);
				  
				  //if there are today matches
				  if(dateMatch.equals("")) {
					  String this_month  = "";
					  if((Calendar.getInstance().get(Calendar.MONTH)+1)<10)
						  this_month = "0" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
					  else this_month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
					  dateMatch = dateMatch.concat(String.valueOf(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))+"/"+this_month);
				  }
				  
				  //replace of spaces in match date
				  dateMatch = dateMatch.replaceAll("\\p{Z}", "");
				  //adding year to the match date
				  dateMatch = dateMatch.concat("/"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
				  
				  String teamlist = matches.get(j+2).getElementsByTag("a").text();
//				  System.out.println(matches.get(j+2).getElementsByTag("a"));
//				  System.out.println(teamlist);
				  //replace of spaces in matches
				  teamlist = teamlist.replaceAll("\\p{Z}", "");
				  
				  //replace of "?" characters
				  String[] teams = teamlist.split("-");
				  
				  match[0] = dateMatch;
				  match[1] = teams[0];
				  try{
				  if(teams[1]!=null)
					  match[2] = teams[1];
				  }
				  catch(ArrayIndexOutOfBoundsException e) {
					  throw new NoOddsException();
				  }
				  total.add(match);
				  
			  }
			  
			  for(int w = 0; w < total.size(); w++) {
				  
				  Map<bet_types, ArrayList<String>> betList = PublicStrings.betTypeList;
				  List<String> betTypeList = betList.get(bt.getBet_type());
				  
				  for(int i = 0; i < betTypeList.size(); i++){
					  int y = (betTypeList.size()*w)+i;
					  BetRow r = new BetRow(total.get(w)[1],total.get(w)[2],
							  Float.parseFloat(quotes.get(y).text()),
							  bt.getBet_type().toString(),
							  total.get(w)[0],
							  bt.getLeague().toString(),
							  bt.getBetter().toString(),
							  betTypeList.get(i),
							  PublicStrings.SIST_PROV,
							  bt.getSport().toString()
							  );
					  listBets.add(r);  
//					  r.printBet();
				  }
			  }
			  
			  return listBets;
		    }   catch (IOException e2) {return null;}
	}

	private static ArrayList<BetRow> getTodayBasketMatches(BetType bt){
		try {
			  String source = getLeagueLink(bt);
			  Document doc = Jsoup.connect(source).get(); 
			  System.out.println("\n" + source + "\n");
			  
			  Elements quotes = doc.getElementsByClass(WH_QUOTE_CLASS);
			  Elements matches = doc.getElementsByClass(WH_BASKET_MATCH_CLASS);
			  
			  ArrayList<BetRow> list = new ArrayList<BetRow>();
			  ArrayList<String[]> total = new ArrayList<String[]>();
			  
			  for(int j= 0; j < matches.size(); j++){
				  
				  String teamlist = matches.get(j).text();
				  
				  teamlist = NBAStrings.removeNbaNicknames(teamlist);
						  
				  teamlist = teamlist.replaceAll("\\p{Z}", "");
				  String[] match = teamlist.split("-");
			
				  total.add(match);
			  }
			  
			  for(int w = 0; w < total.size(); w++) {
				  Calendar now = Calendar.getInstance();
				  DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				  
				  Map<bet_types, ArrayList<String>> betList = PublicStrings.betTypeList;
				  List<String> betTypeList = betList.get(bt.getBet_type());
				  
				  for(int i = 0; i < betTypeList.size(); i++){
					  int y = (betTypeList.size()*w)+i;
					  BetRow r = new BetRow(total.get(w)[0],total.get(w)[1],
							  Float.parseFloat(quotes.get(y).text()),
							  bt.getBet_type().toString(),
							  format.format(now.getTime()),
							  bt.getLeague().toString(),
							  bt.getBetter().toString(),
							  betTypeList.get(i),
							  PublicStrings.SIST_PROV,
							  bt.getSport().toString()
							  );
				  list.add(r);
//				  r.printBet();
				  }
			  }
			  return list;
		    } catch (IOException ex) {
		    	return null;
		    }
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
		case BASKET :  {
			link = link.concat(LINK_BASKET);
			if(bt.getLeague().equals(leagues.NBA))
				link = link.concat(LINK_COD_BASKET_NBA);
			if(bt.getLeague().equals(leagues.A))
				link = link.concat(LINK_COD_BASKET_SERIEA);
			link = link.concat(LINK_END);
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
	
	private static Map<BetType, String> createLinksMap(){
		Map<BetType, String> leagueLinksList = new HashMap<BetType, String>();
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.BL), "3470/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.PL), "343/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.LIGA), "5323/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.A), "4002/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.ED), "2588/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.L1), "3118/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.LP), "4706/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.SPL), "1355/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.SWL), "5499/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.FLV), "3028/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.DSP), "8244175/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.RL1), "4883/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.MLS), "6471/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.NTL), "4442/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.ECS), "1005/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.RPL), "4971/Esito+Finale+1X2.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.ESITO1X2, leagues.TSL), "5852/Esito+Finale+1X2.html");
		
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.A), "94690/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.LIGA), "102020/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.BL), "94689/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.PL), "94685/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.L1), "158127/Goal+No+Goal.html");		
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.LP), "158144/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.ED), "113856/Goal+No+Goal.html");	
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.SWL), "158151/Goal+No+Goal.html");	
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.FLV), "158126/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.DSP), "8244307/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.RL1), "158146/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.MLS), "117895/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.NTL), "158141/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.ECS), "94686/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.TSL), "158138/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.RPL), "122264/Goal+No+Goal.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.GOAL_NOGOAL, leagues.SPL), "97574/Goal+No+Goal.html");
		
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.PL), "43186/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.LIGA), "43367/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.BL), "43319/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.L1), "43315/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.LP), "43351/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.ED), "43305/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.A), "43330/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.SWL), "43370/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.FLV), "43312/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.DSP), "8244268/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.RL1), "64120/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.MLS), "124583/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.NTL), "43344/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.ECS), "38373/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.TSL), "43376/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.RPL), "117908/Doppia+Chance.html");
		leagueLinksList.put(new BetType(sports.CALCIO, betters.WHILL, bet_types.DOUBLE_CHANCE, leagues.SPL), "43196/Doppia+Chance.html");
		
		return leagueLinksList;
	}
	
}

