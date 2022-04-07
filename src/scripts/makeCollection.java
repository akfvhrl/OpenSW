package scripts;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	
	private String data_path;
	private String output_flie = "./collection.xml";
	
	public makeCollection(String path) throws Exception {
		this.data_path = path;
		
		File folder = new File(data_path);
		File files[] = folder.listFiles();
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document document = docBuilder.newDocument();
		
		Element docs = document.createElement("docs");
		document.appendChild(docs);
		
		Element doc[] = new Element[files.length];
		Element title[] = new Element[files.length];
		Element body[] = new Element[files.length];
		
		for(int i =0; i<files.length; i++) {
			doc[i] = document.createElement("doc");	
			docs.appendChild(doc[i]);
			
			doc[i].setAttribute("id", Integer.toString(i));
			
			int count = 0;
			File file = files[i];
			
				try {
					String fileLine ="";
					String inBody ="";
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
					
					while((fileLine = br.readLine()) != null) {
						
						if(fileLine.indexOf("title") >= 0) {
							fileLine=fileLine.replace("<title>","");
							fileLine=fileLine.replace("    ", "");
							fileLine=fileLine.replace("</title>","");
							title[i] = document.createElement("title");
							title[i].appendChild(document.createTextNode(fileLine));
							doc[i].appendChild(title[i]);
						}
						else {
							if(count == 1) {
								if(fileLine.indexOf("<p>")>=0) {
									fileLine=fileLine.replace("<p>","");
									fileLine=fileLine.replace("    ", "");
									fileLine=fileLine.replace("</p>","");
									inBody += fileLine;
								}
							}
							else if(fileLine.indexOf("body") >= 0) {
								count++;
							}
						}
					}
					body[i] = document.createElement("body");
					body[i].appendChild(document.createTextNode(inBody));
					doc[i].appendChild(body[i]);
					br.close();
				} catch(Exception e) {
				}
			
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(new File(this.output_flie)));
		
		transformer.transform(source, result);
		
	}
	
	public void makeXml(){
		System.out.println("2주차 실행완료");
		
	}
	
}


