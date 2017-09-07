package betting.utils;

public class NBAStrings extends PublicStrings {

	public static final int TOTAL_NBA_TEAMS = 30;
	public static final int TOTAL_NBA_NICKNAMES = 28;
	
	public static enum NBA_TEAMS {
		Heat, Magic, Hawks, Bobcats, Wizards, Celtics, Sixers, Raptors, Nets, Knicks, Pacers, Bulls, Bucks, Pistons, Cavaliers,
		Thunder, Jazz, TrailBlazers, Nuggets, Timberwolves, Rockets, Spurs, Mavericks, Grizzlies, Pelicans, Lakers, Clippers, Suns, Warriors, Kings;
	}
	
	
	public static String removeNbaNicknames(String teamlist){
		for (NBAStrings.NBA_TEAMS team : NBAStrings.NBA_TEAMS.values()) {
			   teamlist = teamlist.replace(String.valueOf(team),"");
		}
		return teamlist;
	}
	
}
