package zou_z_l.weighted_doxy;

import org.dom4j.Element;
import org.dom4j.Document;

import java.util.Map;
import java.util.List;


public class PutWeight {
	
	public static void weightMap(List limited_line) {
		XmlParser xmlparser = new XmlParser();
		PutWeight weightmap = new PutWeight();
		Document document = xmlparser.load("/home/zou/zou_z_l/doxygen_output_of_ninegridimageview/xml/_nine_grid_image_view_8java.xml");
		org.dom4j.Element root = document.getRootElement();
		Element sonofroot = root.element("compounddef");
		Element codelines = sonofroot.element("programlisting");
		List<Element> linenumber = codelines.elements();
		for(Element e:linenumber) {
			//System.out.println(e.getName());
			for(Object a:limited_line) {
				if(a.equals(e.attributeValue("lineno"))) {
					List<Element> sonlist = e.elements();
					Element firstson = sonlist.get(0);
					int count = weightmap.countSpace(firstson);
					System.out.println(firstson.asXML());
					System.out.println(count);    
				}
			}
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

}
