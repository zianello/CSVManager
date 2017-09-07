package betting.utils;

import java.util.ArrayList;
import org.jdom2.Element;
import betting.db.entitites.BetRow;

public class XMLElements {
	
	private enum xml_elements {
		HOME, AWAY, DATE, QUOTA, TS_INS, LEAGUE, BETTER, BET_TYPE, SIST_PROV, PREDICTION
	}

	public static Element createXMLElement(BetRow tr, String sport){
		
		Element e = new Element(sport);
		
		ArrayList<String> contentList = new ArrayList<String>();
		
		//PK
		contentList.add(tr.getHome());
		contentList.add(tr.getAway());
		contentList.add(tr.getDate());
		
		contentList.add(String.valueOf(tr.getQuote()).replaceAll("\\.", "\\,"));
		contentList.add(tr.getIns_time());
		contentList.add(tr.getLeague());
		contentList.add(tr.getBetter());
		contentList.add(tr.getBet_type());
		contentList.add(tr.getSist_prov());
		contentList.add(tr.getPrediction());
		
		ArrayList<Element> elemList = new ArrayList<Element>();
		int i = 0;
		for(xml_elements el : xml_elements.values()){
			elemList.add(new Element(el.toString()));
			elemList.get(i).addContent(contentList.get(i));
			i++;
		}
	
		e.setContent(elemList);
		return e;
	}
}
