package zou_z_l.weighted_doxy;

import org.dom4j.Element;
import org.dom4j.Document;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;


public class PutWeight {
	private static int count = 0;
	private static Map finalweight = new HashMap();
	public static void weightMap(List limited_line) {
		XmlParser xmlparser = new XmlParser();
		PutWeight weightmap = new PutWeight();
		ArrayList elementline = new ArrayList();
		Map looptimes = new HashMap();
		Map weighted_map = new HashMap();
		Document document = xmlparser.load("/home/zou/zou_z_l/nine2/xml/_view_server_8java.xml");
		org.dom4j.Element root = document.getRootElement();
		Element sonofroot = root.element("compounddef");
		Element codelines = sonofroot.element("programlisting");
		List<Element> linenumber = codelines.elements();
		for(Element e:linenumber) {
			//System.out.println(e.getName());
			for(Object a:limited_line) {
				if(a.equals(e.attributeValue("lineno"))) {
					elementline.add(e);
					ArrayList starttoend = xmlparser.startToEnd("/home/zou/zou_z_l/nine2/xml/classcom_1_1jaeger_1_1ninegridimgdemo_1_1_view_server.xml","run");
					List<Element> sonlist = e.elements();
					Element firstson = sonlist.get(0);
					count = weightmap.countSpace(firstson);
					weighted_map.put(e, count);
					looptimes.put(e, (int)0);
					weightmap.justweightit(xmlparser, e, count, starttoend, linenumber,weighted_map,looptimes);
					//System.out.println(firstson.asXML());         
					//System.out.println(count);  
				}
			}
		}
		System.out.println("looptimes"+looptimes);
		System.out.println("finalweight"+finalweight);
	}
	
	
	private void justweightit(XmlParser xmlparser,Element e,int count,ArrayList starttoend,List<Element> linenumber,Map weighted_map,Map looptimes) {
		  finalweight.put(e, 100);
		  String tmp = e.attributeValue("lineno");
		  int line_num = Integer.parseInt(tmp);
		  boolean compare = true; 
		  while(compare) {
			  this.subtraction(line_num, linenumber,e,weighted_map,looptimes);                                            
			  compare = this.compareNumber(line_num-1, starttoend);
			  line_num = line_num-1;
		  }
		  
	}
	
	private boolean compareNumber(int line_num,ArrayList starttoend) {
		Object get0 = starttoend.get(0);
		Object get1 = starttoend.get(1);
		String m0 = String.valueOf(get0);
		int min = Integer.parseInt(m0);
		if(line_num>min) {
			return true;
		}else {
			return false;
		}
	}
	
	private int countSpace(Element firstson) {
		String xml_str = firstson.asXML();
		int count = 0;
		while(xml_str.indexOf("<sp/>")!=-1) {
			xml_str = xml_str.substring(xml_str.indexOf("<sp/>")+1,xml_str.length());
			count++;
		}
		//System.out.println("<sp/>出现的次数为:"+count+"次");
		return count;
	}
	
	private void subtraction(int line_num,List<Element> linenumber,Element e,Map weighted_map,Map looptimes) {
		int new_line_num = line_num-1;
		String new_line = String.valueOf(new_line_num);
		for(Element a:linenumber) {
			if(new_line.equals(a.attributeValue("lineno"))) {
				//System.out.println(a);
				List<Element> sonlist = a.elements();
				Element firstson = sonlist.get(0);
				PutWeight tmp = new PutWeight();
				count = tmp.countSpace(firstson);
				changeWeight(count,weighted_map,e,a,looptimes);
			}
		} 
	}
	
	private void changeWeight(int count,Map weighted_map,Element e,Element a,Map looptimes) {
		Object m0 = weighted_map.get(e);
		String m1 = String.valueOf(m0);
		int oldcount = Integer.parseInt(m1);
		if (oldcount<=count) {
			return;
		}else if(oldcount>count){
			String val = String.valueOf(looptimes.get(e));      
			int value = Integer.parseInt(val);
			value+=1;      
			weighted_map.put(e, count);
			looptimes.put(e,value);
			finalWeight(a,e);
			
		}	
	}
	
	private void finalWeight(Element a,Element e) {
		List<Element> keywords = a.elements();
		for(Element i:keywords) {
			if(i.getText().equals("if")) {
				Object tmp1 = finalweight.get(e);
				String tmp2 = String.valueOf(tmp1);
				int oldvalue = Integer.parseInt(tmp2);
				int newvalue = oldvalue/2;
				finalweight.put(e, newvalue);
			}else if(i.getText().equals("for")){
				Object tmp1 = finalweight.get(e);
				String tmp2 = String.valueOf(tmp1);
				int oldvalue = Integer.parseInt(tmp2);
				int newvalue = oldvalue*2;
				finalweight.put(e, newvalue);
			}else if(i.getText().equals("while")){
				Object tmp1 = finalweight.get(e);
				String tmp2 = String.valueOf(tmp1);
				int oldvalue = Integer.parseInt(tmp2);
				double newvalue = oldvalue*1.5;
				finalweight.put(e, newvalue);
			}else if(i.getText().equals("switch")){
				Object tmp1 = finalweight.get(e);
				String tmp2 = String.valueOf(tmp1);
				int oldvalue = Integer.parseInt(tmp2);
				double newvalue = oldvalue/4;
				finalweight.put(e, newvalue);
			}else if(i.getText().equals("else if")){
				Object tmp1 = finalweight.get(e);
				String tmp2 = String.valueOf(tmp1);
				int oldvalue = Integer.parseInt(tmp2);
				double newvalue = oldvalue/3;
				finalweight.put(e, newvalue); 
			}
		}
	}
}










