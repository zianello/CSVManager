package betting.utils;

import betting.db.entitites.BetRow;

public class CSVElements {
	
	public static String createCSVElement(BetRow tr, String sport){
		String list = new String();
		
		//PK
		list = addSeparator(list.concat(tr.getHome()));
		list = addSeparator(list.concat(tr.getAway()));
		list = addSeparator(list.concat(tr.getDate()));

		list = addSeparator(list.concat(String.valueOf(tr.getQuote()).replaceAll("\\.", "\\,")));
		list = addSeparator(list.concat(tr.getIns_time()));
		list = addSeparator(list.concat(tr.getLeague()));
		list = addSeparator(list.concat(tr.getBetter()));
		list = addSeparator(list.concat(tr.getBet_type()));
		list = addSeparator(list.concat(tr.getSist_prov()));
		list = addSeparator(list.concat(tr.getPrediction()));
		list = addSeparator(list.concat(sport));
		
		list = list + "\n";
		
		System.out.print(list);
		return list;
	}
	
	private static String addSeparator(String s){
		s = s.concat(";");
		return s;
	}

}
