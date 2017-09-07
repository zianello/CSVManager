package betting.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import betting.utils.PublicStrings.BetType;

public class Utils {

	public static void printString(Object[] object){
		for(int h = 0; h < object.length ; h++)
			  System.out.println(object[h]);	
	}
	
	public static String convertMonths(String month){
		for(int w = 1; w <= PublicStrings.TOTAL_MONTHS; w++){
				if(w < 10) month = month.replace(PublicStrings.monthList.get(w-1),"/0"+String.valueOf(w));
				else month = month.replace(PublicStrings.monthList.get(w-1),"/"+String.valueOf(w));
			  	}
		return month;
	}
	
	public static Date now(){
		return Calendar.getInstance().getTime();
	}
	
	public static int getNumFromNow(int input){
		Calendar cal = Calendar.getInstance();
		cal.setTime(now());
		switch(input){
			case Calendar.YEAR:
				return cal.get(Calendar.YEAR);
			case Calendar.MONTH:
				return cal.get(Calendar.MONTH)+1;
			case Calendar.DAY_OF_MONTH:
				return cal.get(Calendar.DAY_OF_MONTH);
			case Calendar.DAY_OF_WEEK:
				return cal.get(Calendar.DAY_OF_WEEK);
			case Calendar.HOUR_OF_DAY:
				return cal.get(Calendar.HOUR_OF_DAY);
			case Calendar.MINUTE:
				return cal.get(Calendar.MINUTE);
			case Calendar.SECOND:
				return cal.get(Calendar.SECOND);
			case Calendar.MILLISECOND:
				return cal.get(Calendar.MILLISECOND);
			default:
				return 0;
		}
	}
	
	public static String createCSVfileName(BetType bt){
		String link = PublicStrings.getPathFile();
		link = link.concat(PublicStrings.getPathFolder(bt.getSport()));
		link = link.concat("//" + bt.getSport().toString());
		link = link.concat("_" + bt.getBetter().toString());
		link = link.concat("_" + bt.getBet_type().toString());
		link = link.concat("_" + getNumFromNow(Calendar.YEAR));
		link = link.concat("_" + getNumFromNow(Calendar.MONTH));
		link = link.concat("_" + getNumFromNow(Calendar.DAY_OF_MONTH));
		link = link.concat("_" + getNumFromNow(Calendar.HOUR_OF_DAY));
		link = link.concat("_" + getNumFromNow(Calendar.MINUTE) +
										 "_" + getNumFromNow(Calendar.SECOND));
		link = link.concat(PublicStrings.getCSVtype());	
		return link;
	}
	
	public static void fileCreateOrAppend(String filePath, List<String> toWrite) throws FileNotFoundException{
		File csvFile = new File(filePath);
		PrintWriter w = null;
		if(csvFile.exists() && !csvFile.isDirectory()){
			w = new PrintWriter(new FileOutputStream(csvFile, true));
		}
		else{
			w = new PrintWriter(csvFile);
		}
		for(String wr : toWrite){
//			Element todayMatch = XMLElements.createXMLElement(today.get(u), bt.getSport().toString());
//			rootNode.addContent(todayMatch);
			w.append(wr);
		}

		w.close();
	}
	
}

