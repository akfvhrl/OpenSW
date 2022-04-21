package scripts;

import org.snu.ids.kkma.index.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class MidTerm {
    private String input_file;
    private String input_word;

    public MidTerm(){


    }

    public void showSnippet(String path, String query ) throws Exception {
        this.input_file = path;
        this.input_word = query;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document documentKkma = db.parse(new File(this.input_file));


        try {
            NodeList nList = documentKkma.getElementsByTagName("doc");
            String[] title = new String[nList.getLength()];
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if(nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    title[temp] = eElement.getElementsByTagName("title").item(0).getTextContent();
                }
            }

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String bodyKkma = eElement.getElementsByTagName("body").item(0).getTextContent();

                    KeywordExtractor ke = new KeywordExtractor();

                    KeywordList kl = ke.extractKeyword(this.input_word, true);
                    String[] kwrdlist = new String[kl.size()];
                    for( int i = 0; i<kl.size(); i++) {
                        Keyword kwrd = kl.get(i);
                        kwrdlist[i] = kwrd.getString();
                    }
                    for( int i = 0; i<kl.size(); i++) {
                        if(bodyKkma.indexOf(kwrdlist[i])>=0){
                            System.out.println(bodyKkma.split(bodyKkma.indexOf(kwrdlist[i]),30));
                        }
                    }

                }
            }

        }
        catch(Exception e) {

        }
    }
}
