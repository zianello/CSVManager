package betting.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PublicStrings {
//	private static String FILE_PATH_INIT = "";
	private static final String FILE_PATH = "";
	private static final String XML_EXTENSION = ".xml";
	private static final String CSV_EXTENSION = ".csv";
	public static final String SIST_PROV = "JAVA_WEB_APP";
	public static final int TOTAL_MONTHS = 12;
	
	public static final Map<sports, String> file_paths = initializeSportMap();
	
	public static final Map<Integer, String> monthList = initializeMonthsMap();
	
	public static final Map<bet_types, ArrayList<String>> betTypeList = initBetTypeMap();
	
	public static enum sports {
		CALCIO,
		BASKET;
	}
	
	public static enum leagues {
		A, 
		PL,
		BL,
		LIGA,
		L1,
		ED,
		SPL,
		LP,
		NBA,	
		SWL,	//Svezia	Allsvenskan
		FLV,	//Finlandia	Veikkausliiga
		DSP,	//Danimarca SuperLeague
		RL1,	//Romania L1
		MLS,	//USA MLS
		NTL,	//Norvergia Tippeligaen
		ECS,	//England Championship	
		RPL,	//Russian Premier League
		TSL		//Turkish Super Lig
	};
	
	public static enum betters {
		WHILL,
		BETFAIR, 	//Betfair Normale
		BETFAIRexc, //Betfair Exchange
		BWIN,
		SNAI,
		BET365,
		BETCLIC,
		BETTER,
		EUROBET
	}
	
	public static enum bet_types {
		ESITO1X2,
		ESITO12,
		GOAL_NOGOAL,
		DOUBLE_CHANCE
	}
	
	public static String getPathFile(){
		return FILE_PATH;
	}
	
	public static String getPathFolder(PublicStrings.sports sp){
		return file_paths.get(sp);
	}
	
	public static String getXMLtype(){
		return XML_EXTENSION;
	}
	
	public static String getCSVtype(){
		return CSV_EXTENSION;
	}
	
	private static Map<sports, String> initializeSportMap(){
		Map<sports, String> list = new HashMap<PublicStrings.sports, String>();
		list.put(sports.CALCIO, "Soccer");
		list.put(sports.BASKET, "Basket");
		return list;
	}
	
	private static Map<Integer, String> initializeMonthsMap(){
		Map<Integer, String> list = new HashMap<Integer, String>();
		list.put(Calendar.JANUARY, "Gen");
		list.put(Calendar.FEBRUARY, "Feb");
		list.put(Calendar.MARCH, "Mar");
		list.put(Calendar.APRIL, "Apr");
		list.put(Calendar.MAY, "Mag");
		list.put(Calendar.JUNE, "Giu");
		list.put(Calendar.JULY, "Lug");
		list.put(Calendar.AUGUST, "Ago");
		list.put(Calendar.SEPTEMBER, "Set");
		list.put(Calendar.OCTOBER, "Ott");
		list.put(Calendar.NOVEMBER, "Nov");
		list.put(Calendar.DECEMBER, "Dic");
		return list;
	}
	
	private static Map<bet_types, ArrayList<String>> initBetTypeMap(){
		Map<bet_types, ArrayList<String>> list = new HashMap<bet_types, ArrayList<String>>();
		list.put(PublicStrings.bet_types.ESITO12, new ArrayList<String>(Arrays.asList("1", "2")));
		list.put(PublicStrings.bet_types.ESITO1X2,new ArrayList<String>(Arrays.asList("1", "X", "2")));
		list.put(PublicStrings.bet_types.GOAL_NOGOAL,new ArrayList<String>(Arrays.asList("GOAL", "NOGOAL")));
		list.put(PublicStrings.bet_types.DOUBLE_CHANCE,new ArrayList<String>(Arrays.asList("1X", "X2", "12")));
		return list;
	}
	
	public static class BetType {
		
		private PublicStrings.sports sport;
		
		private PublicStrings.betters better;
		
		private PublicStrings.bet_types bet_type;
		
		private PublicStrings.leagues league;
		
		public BetType (){
		}
		
		public BetType (PublicStrings.sports sport, PublicStrings.betters better, 
				PublicStrings.bet_types bet_type, PublicStrings.leagues league ){
			this.sport = sport;
			this.better = better;
			this.bet_type = bet_type;
			this.league = league;
		}
		
		public BetType (PublicStrings.sports sport, PublicStrings.betters better, 
				PublicStrings.bet_types bet_type ){
			this.sport = sport;
			this.better = better;
			this.bet_type = bet_type;
		}

		public PublicStrings.sports getSport() {
			return sport;
		}

		public void setSport(PublicStrings.sports sport) {
			this.sport = sport;
		}

		public PublicStrings.betters getBetter() {
			return better;
		}

		public void setBetter(PublicStrings.betters better) {
			this.better = better;
		}

		public PublicStrings.bet_types getBet_type() {
			return bet_type;
		}

		public void setBet_type(PublicStrings.bet_types bet_type) {
			this.bet_type = bet_type;
		}

		public PublicStrings.leagues getLeague() {
			return league;
		}

		public void setLeague(PublicStrings.leagues league) {
			this.league = league;
		}
		
		@Override
		public boolean equals(Object bt){
			if(bt == null) return false;
			if(!(bt instanceof BetType)) return false;
			if(this.league.equals(((BetType)bt).getLeague()) &&
					this.sport.equals(((BetType)bt).getSport()) &&
					this.better.equals(((BetType)bt).getBetter()) &&
					this.bet_type.equals(((BetType)bt).getBet_type()))
				return true;
			return false;
		}
		
		@Override
		public int hashCode(){
			return this.bet_type.hashCode() + this.better.hashCode() + 
						 this.league.hashCode() + this.sport.hashCode();
		}
	}
	
}
