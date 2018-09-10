
package zou_z_l.weighted_doxy;


import org.dom4j.Element;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


public class XmlParser {
	public Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(filename));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	
	public List<Element> findLocation(List edgetext){
		XmlParser xmlparser = new XmlParser();
		Document xml = load("/home/zou/zou_z_l/doxygen_output_of_ninegridimageview/xml/_nine_grid_image_view_8java.xml");
		org.dom4j.Element root = xml.getRootElement();
		Element sonofroot = root.element("compounddef");
		Element codelines = sonofroot.element("programlisting");
		List<Element> linenumber = codelines.elements();
		ArrayList chosen_line = new ArrayList();
		for(Object edge:edgetext) {
			String edge_str = edge.toString();
			for(Element e:linenumber) {
				//System.out.println(e.getName());
				//System.out.println(e.elements());
				List<Element> each_line = e.elements();
				for(Element i:each_line) {
					//System.out.println(i.getText());
					String text_str = i.getText();
					if(text_str.contains(edge_str)) {
						chosen_line.add(e.attributeValue("lineno"));
						//System.out.println(chosen_line);
						//System.out.println(e.attributeValue("lineno"));
						//System.out.println(i.asXML());
					}
				}
			}
		}
		System.out.println("chosen_line"+chosen_line);
		ArrayList starttoend = xmlparser.startToEnd("/home/zou/zou_z_l/doxygen_output_of_ninegridimageview/xml/classcom_1_1jaeger_1_1ninegridimageview_1_1_nine_grid_image_view.xml","layoutChildrenView");
		ArrayList limited_line = xmlparser.lineFilter(chosen_line,starttoend);
		return limited_line;
		
	}
	
	     
	public ArrayList startToEnd(String directory,String node) {
		ArrayList starttoend = new ArrayList();
		Document dir = load(directory);
		org.dom4j.Element root = dir.getRootElement();
		Element compounddef = root.element("compounddef");
		List<Element> son = new TryParser().getsonElement(compounddef);
		for(Element a:son) {
			List<Element> son2 = a.elements();
			for (Element b:son2) {
				List<Element> son3 = b.elements();
				for(Element c:son3) {
					//System.out.println(c.getName());
					if(node.equals(c.getText())) {
						//System.out.println(c.getText());
						for(Element d:son3) {
							if("location".equals(d.getName())) {
								//System.out.println(d.attributeValue("bodystart"));
								//System.out.println(d.attributeValue("bodyend"));
								starttoend.add(d.attributeValue("bodystart"));
								starttoend.add(d.attributeValue("bodyend"));
							}
						}
					}
				}
			}
		}
		System.out.println("starttoend"+starttoend);
		return starttoend;
	}
	private ArrayList lineFilter(ArrayList chosen_line,ArrayList starttoend) {
		Object get0 = starttoend.get(0);
		Object get1 = starttoend.get(1);
		String m0 = String.valueOf(get0);
		String m1 = String.valueOf(get1);
		int min = Integer.parseInt(m0);
		int max = Integer.parseInt(m1);
		ArrayList limited_line = new ArrayList();
		limited_line.addAll(chosen_line);
		for(Object e:chosen_line) {
			String str_e = String.valueOf(e);
			int int_e = Integer.parseInt(str_e);
			if(int_e<min) {
				limited_line.remove(e);
			}else if(int_e>max) {
				limited_line.remove(e);
			}
		}
		System.out.println("limited_line"+limited_line);
		return limited_line;
		  
	}
}



