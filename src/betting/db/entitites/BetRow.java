package betting.db.entitites;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BetRow {

	private String home;
	
	private String away;
	
	private float quote;
	
	private String bet_type;

	private String ins_time;
	
	private String date;
	
	private String league;
	
	private String better;
	
	private String prediction;
	
	private String sist_prov;
	
	private String sport;
	
	public BetRow(){
		
	}
	
	public BetRow(String home, String away, float quote, String bet_type, String date, String league, String better,
			String prediction, String sist_prov, String sport){
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		this.home = home;
		this.away = away;
		this.quote = quote;
		this.bet_type = bet_type;
		this.date = date;
		this.league = league;
		this.better = better;
		this.prediction = prediction;
		this.sist_prov = sist_prov;
		this.ins_time = format.format(Calendar.getInstance().getTime());
		this.sport = sport;
	}
	

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getAway() {
		return away;
	}

	public void setAway(String away) {
		this.away = away;
	}

	public float getQuote() {
		return quote;
	}

	public void setQuote(float quote) {
		this.quote = quote;
	}

	public String getBet_type() {
		return bet_type;
	}

	public void setBet_type(String bet_type) {
		this.bet_type = bet_type;
	}

	public String getIns_time() {
		return ins_time;
	}

	public void setIns_time(String ins_time) {
		this.ins_time = ins_time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getBetter() {
		return better;
	}

	public void setBetter(String better) {
		this.better = better;
	}

	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

	public String getSist_prov() {
		return sist_prov;
	}

	public void setSist_prov(String sist_prov) {
		this.sist_prov = sist_prov;
	}
	
	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public void printBet(){
		System.out.print(this.getHome()+ " ");
		System.out.print(this.getAway()+ " ");
		System.out.print(this.getDate()+ " ");
		System.out.print(this.getQuote()+ " ");
		System.out.print(this.getBet_type()+ " ");
		System.out.print(this.getIns_time()+ " ");
		System.out.print(this.getLeague()+ " ");
		System.out.print(this.getBetter()+ " ");
		System.out.print(this.getPrediction()+ " ");
		System.out.print(this.getSport()+ " ");
		System.out.println(this.getSist_prov()+ " ");
		
	}
}