package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

public class indexer {

	private String input_file;
	private String output_file = "./index.post";
	
	public indexer(String path) throws Exception {
		this.input_file = path;
		
		ArrayList<HashMap<String, Integer>> dataArray = new ArrayList<>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File(this.input_file));
		
		try {
            NodeList nList = document.getElementsByTagName("doc");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
            	HashMap<String, Integer> data = new HashMap<>();

            	Node nNode = nList.item(temp);          	
            	if(nNode.getNodeType() == Node.ELEMENT_NODE) {
            		Element eElement = (Element) nNode;
            		String body = eElement.getElementsByTagName("body").item(0).getTextContent();
            		String[] bodyArr = body.split("#");
            		for(String s : bodyArr) {
            			data.put(s.split(":")[0], Integer.parseInt(s.split(":")[1]));
            		}
            		dataArray.add(data);
            	}
            }
            
            HashMap<String, HashMap<Integer, Double>> result = new HashMap<>();
            
            for(int i = 0; i < dataArray.size(); i++) {
            	HashMap<String, Integer> data = dataArray.get(i);
            	for(String targetWord : data.keySet()) {
            		if(!result.containsKey(targetWord)) {
            			HashMap<Integer, Double> hash = new HashMap<>();
            			hash.put(0,0.0);
                        hash.put(1,0.0);
                        hash.put(2,0.0);
                        hash.put(3,0.0);
                        hash.put(4,0.0);
                        result.put(targetWord, hash);
            		}
            		
            		double tf = 0;
            		double idf = 0;
            		double tfIdf = 0;
            		
            		tf = (double) data.get(targetWord);
            		
            		for(HashMap<String,Integer> hash : dataArray) {
                        if(hash.containsKey(targetWord)){
                            idf++;
                        }
                    }
            		idf = dataArray.size()/idf;
            		idf = Math.log(idf);
            		tfIdf = tf*idf;
            		tfIdf = Math.round(tf*idf*100) / 100.0;
            		
            		result.get(targetWord).put(i, tfIdf);
            	}
            }
            FileOutputStream fileStream = new FileOutputStream(this.output_file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
            
            objectOutputStream.writeObject(result);
            
            objectOutputStream.close();
            
            
            
               
		}
		catch(Exception e) {
			
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	public void indexXml(){
		System.out.println("4주차 실행완료");
		
	}
}
