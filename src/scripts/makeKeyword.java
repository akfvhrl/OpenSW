package scripts;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class makeKeyword {

	private String input_file;
	private String output_file = "./index.xml";
	
	public makeKeyword(String file) throws Exception {
		this.input_file = file;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document documentKkma = db.parse(new File(this.input_file));
		
		try {
            NodeList nList = documentKkma.getElementsByTagName("doc");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
            	
            	Node nNode = nList.item(temp);          	
            	if(nNode.getNodeType() == Node.ELEMENT_NODE) {
            		Element eElement = (Element) nNode;
            		String bodyKkma = eElement.getElementsByTagName("body").item(0).getTextContent();
            		String bodyEnd = "";
            		
            		KeywordExtractor ke = new KeywordExtractor();
            		
            		KeywordList kl = ke.extractKeyword(bodyKkma, true);
            		
            		for( int i = 0; i<kl.size(); i++) {
            			Keyword kwrd = kl.get(i);
            			bodyEnd += kwrd.getString() + ":" + kwrd.getCnt() +"#";
            		}
            		
            	documentKkma.getElementsByTagName("body").item(temp).setTextContent(bodyEnd);
            	}
            }
               
		}
		catch(Exception e) {
			
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		
		DOMSource sourceKkma = new DOMSource(documentKkma);
        StreamResult resultKkma = new StreamResult(new FileOutputStream(new File(this.output_file)));
		
		transformer.transform(sourceKkma, resultKkma);
		
	
	}

	public void convertXml() {
		System.out.println("3주차 실행완료");
	}

}
