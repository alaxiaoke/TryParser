package zou_z_l.weighted_doxy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;


import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;



public class TryParser  {
	
	public static List<Element> sonlist ;
	
	public static Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(filename));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	public static void main(String[] args) {
		Document first_doxy = load("/home/zou/zou_z_l/NineGridImageView-master.graphml");
		org.dom4j.Element root = first_doxy.getRootElement();
		Element graph = root.element("graph");
		//System.out.println(graph.getName());
		TryParser tryparser = new TryParser();
		List<Element> son = tryparser.getsonElement(graph);
		String chosen_ID = tryparser.getGrandson(son);
		//System.out.println(chosen_ID);
		ArrayList target_list = tryparser.getEdge(chosen_ID);
		//System.out.println(target_list);
		List edgetext = tryparser.getEdgeText(target_list, son);
		System.out.println("edgetext"+edgetext);
		XmlParser xmlparser = new XmlParser();
		List limited_line = xmlparser.findLocation(edgetext);
		PutWeight putweight = new PutWeight();
		putweight.weightMap(limited_line);  

	}
	
	
	
	public List<Element> getsonElement(Element graph) {
		int i = 0;
		if (graph.elements().size()!=0) {
			//System.out.println(graph.getName()+"\nis consist of:");
			sonlist = graph.elements();
			for(org.dom4j.Element e:sonlist){
				i = i+1;
			}
		}else {
			System.out.println("没有子节点");
		}	
		return sonlist; 
	}
	
	
	private String getGrandson(List<Element> son) {
		String chosen_ID="null";
		for (Element e:son) {
			String node_data = e.attributeValue("id");
			List<Element> grandsonList = e.elements();
			for(Element a:grandsonList) {
				if("com.jaeger.ninegridimageview.NineGridImageView.layoutChildrenView".equals(a.getText())){
					//System.out.println(node_data);
					chosen_ID = node_data;
					break;
					}
				}
				//System.out.println(grandsonList);
			}
		return chosen_ID;
	}	
	private ArrayList getEdge(String chosen_ID){
		ArrayList get_target = new ArrayList();
		for(Element e:sonlist) {
			if("edge".equals(e.getName())) {
				String source = e.attributeValue("source");
				if(chosen_ID.equals(source)) {
					get_target.add(e.attributeValue("target"));
				}
			}
		}
		if (get_target.size()==0) {
			System.out.println("该节点没有调用其他节点");
		}
		return get_target;
	}
	private List getEdgeText(ArrayList target_list,List<Element> son) {
		List edgetext = new ArrayList();
		for(int i=0;i<target_list.size();i++) {        
			for(Element e:son) {
				String target_i = String.valueOf(target_list.get(i));
				if(target_i.equals(e.attributeValue("id"))) {
				 	List<Element> nodes = e.elements();
				 	for (Element node:nodes) {
				 		if("D0".equals(node.attributeValue("key"))) {
				 			edgetext.add(node.getText());
				 		}
				 	}
				}
			}
		}
		return edgetext;
	}
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	


	