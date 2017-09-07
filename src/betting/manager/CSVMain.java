package betting.manager;

import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import betting.betters.BetFair;
import betting.betters.BetFairExchange;
import betting.betters.WilliamHill;
import betting.db.entitites.BetRow;
import betting.exceptions.BetterNotFoundException;
import betting.exceptions.NoOddsException;
import betting.utils.CSVElements;
import betting.utils.PublicStrings;
import betting.utils.PublicStrings.BetType;
import betting.utils.Utils;


public class CSVMain {

	//args[0]: sport
	//args[1]: better
	//args[2]: bet_type
	//args[3]: league
	public static void main(String[] args) throws BetterNotFoundException{

		System.out.println("start time: " + Calendar.getInstance().getTime());
		//PublicStrings.sports.valueOf(args[0]); //serve per testare l'eccezione ArrayIndexOutOfBoundSException
		startCommandLine(args);
		//fakeCommandLine();
		//startVisualBox();
		System.out.println("end time: " + Calendar.getInstance().getTime());

	}

	public static void fakeCommandLine() throws BetterNotFoundException{
		String[] args = new String[3];
		args[0] = PublicStrings.sports.CALCIO.toString();
		args[1] = PublicStrings.betters.WHILL.toString();
		args[2] = PublicStrings.bet_types.ESITO1X2.toString();
		startCommandLine(args);
	}

	public static void startCommandLine(String[] args) throws BetterNotFoundException{

		BetType bt = new BetType();
		try{
			PublicStrings.leagues.valueOf(args[3]); //serve per testare il try catch
			bt = new BetType(PublicStrings.sports.valueOf(args[0]), 
			PublicStrings.betters.valueOf(args[1]),
			PublicStrings.bet_types.valueOf(args[2]),
			PublicStrings.leagues.valueOf(args[3]));
		}
		catch(ArrayIndexOutOfBoundsException e1){
			bt = new BetType(PublicStrings.sports.valueOf(args[0]), 
			PublicStrings.betters.valueOf(args[1]),
			PublicStrings.bet_types.valueOf(args[2]));
		};
			Update_Odds(bt);
	}

	

	public static void startVisualBox() throws BetterNotFoundException{

		JComboBox<PublicStrings.sports> sport = new JComboBox<PublicStrings.sports>(PublicStrings.sports.values());
		JComboBox<PublicStrings.betters> better = new JComboBox<PublicStrings.betters>(PublicStrings.betters.values());
		JComboBox<PublicStrings.bet_types> bet_type = new JComboBox<PublicStrings.bet_types>(PublicStrings.bet_types.values());
	    JPanel panelSport = new JPanel(new GridLayout(0, 1));

	    panelSport.add(new JLabel("Scegli sport:"));
	    panelSport.add(sport);
	    panelSport.add(new JLabel("Scegli casa:"));
	    panelSport.add(better);
	    panelSport.add(new JLabel("Scegli tipo di scommessa:"));
	    panelSport.add(bet_type);

	    int result = JOptionPane.showConfirmDialog(null, panelSport, "BET_PROJECT", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

	    if (result == JOptionPane.OK_OPTION) {
	    	JComboBox<PublicStrings.leagues> league = new JComboBox<PublicStrings.leagues>(PublicStrings.leagues.values());
	    	JPanel panel = new JPanel(new GridLayout(0, 1));
	    	panel.add(new JLabel("Scegli lega:"));
	    	panel.add(league);
	    	
	    	int resultLeague = JOptionPane.showConfirmDialog(null, panel, "LEGA",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    	
	    	BetType bt = new BetType(PublicStrings.sports.valueOf(sport.getSelectedItem().toString()),
	    			PublicStrings.betters.valueOf(better.getSelectedItem().toString()),
	    			PublicStrings.bet_types.valueOf(bet_type.getSelectedItem().toString()),
	    			PublicStrings.leagues.valueOf(league.getSelectedItem().toString()));

		    if (resultLeague == JOptionPane.OK_OPTION)
		    	Update_Odds(bt);

	    }

	 }

	public static void Update_Odds(BetType bt) throws BetterNotFoundException{
		String filePath = Utils.createCSVfileName(bt);
		if(bt.getLeague() != null )
			try{
				UpdateFile(bt, filePath);
			}
			catch (NoOddsException e) {} 
		else for (PublicStrings.leagues league : PublicStrings.leagues.values()) {
					bt.setLeague(league);
					try{
						UpdateFile(bt, filePath);
					}
					catch (NoOddsException e) {} 
					}
		System.out.println("______________________________________________");

	}

	public static void UpdateFile(BetType bt, String filePath) throws BetterNotFoundException, NoOddsException{

		try{
			ArrayList<BetRow> today = new ArrayList<BetRow>();
			
			switch(bt.getBetter()) {
				case WHILL : 
							 today = WilliamHill.getTodayMatches(bt);
//							 filePath = WilliamHill.getFilePath(bt);
							 break;
				case BETFAIRexc : 
							 today = BetFairExchange.getTodayMatches(bt);
//							 filePath = BetFairExchange.getFilePath(bt);
							 break;
				case BETFAIR : 
							 today = BetFair.getTodayMatches(bt);
//							 filePath = BetFair.getFilePath(bt);
							 break;
				default : throw new BetterNotFoundException();
			}

			createCSVfile(today, bt, filePath);
			
			System.out.println("\n" + "File "+filePath+ " updated!");

		  } catch (FileNotFoundException e1){}
			catch (NullPointerException e1){}
//			catch (JDOMException e) {e.printStackTrace();}
	}
	
	public static void createCSVfile(ArrayList<BetRow> today, BetType bt, String filePath) throws FileNotFoundException{
		ArrayList<String> toWrite = new ArrayList<String>();
		for(BetRow br : today){
			toWrite.add(CSVElements.createCSVElement(br, bt.getSport().toString()));
		}
		Utils.fileCreateOrAppend(filePath, toWrite);
	}
	
}


