package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;





public class searcher {

	private String input_file;
	private String input_word;

	public searcher(String path, String query ) throws Exception {

		this.input_file = path;
		this.input_word = query;

		FileInputStream fileStream = new FileInputStream(this.input_file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

		Object object = objectInputStream.readObject();
		objectInputStream.close();

		HashMap hashMap = (HashMap)object;

		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(this.input_word, true);

		Double[] innerProduct = InnerProduct(kl, hashMap);
		Double[] calcSim = CalcSim(innerProduct, kl, hashMap);


		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document documentKkma = db.parse(new File("./collection.xml"));

		String title = new String();
		Map<String, Double> map = new HashMap<>();
		try {
			NodeList nList = documentKkma.getElementsByTagName("doc");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					title = eElement.getElementsByTagName("title").item(0).getTextContent();

					map.put(title, (calcSim[temp]) );

				}

			}

		}
		catch(Exception e) {

		}

		List<Map.Entry<String, Double>> entryList = new LinkedList<>(map.entrySet());
		entryList.sort(new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				double temp = (o2.getValue() - o1.getValue())*100;
				return (int) temp;
			}
		});
		int cnt = 0;
		for(Map.Entry<String, Double> entry : entryList){
			if(entry.getValue() == 0) {
				System.out.println("????????? ????????? ????????????.");
				break;
			}
			System.out.println( entry.getKey()+ entry.getValue());
			cnt++;
			if(cnt >= 3) {
				break;
			}
		}

	}

	public Double[] InnerProduct(KeywordList kl, HashMap hashMap) {
		Double [][] array = new Double[kl.size()][((HashMap) hashMap.get(kl.get(0).getString())).size()];
		for (int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);

			for (int j = 0; j < ((HashMap) hashMap.get(kl.get(i).getString())).size(); j++) {
				array[i][j] = kwrd.getCnt() * (Double) ((HashMap) hashMap.get(kwrd.getString())).get(j);
			}
		}
		Double[] Q_id = new Double[((HashMap) hashMap.get(kl.get(0).getString())).size()];

		for (int i = 0; i < ((HashMap) hashMap.get(kl.get(0).getString())).size(); i++) {
			Q_id[i] = (double)0;
			for (int j = 0; j < kl.size(); j++) {
				Q_id[i] += array[j][i];
			}
		}
		return Q_id;
	}
	
	public Double[] CalcSim(Double[] innerProdcuct, KeywordList kl, HashMap hashMap){
		Double[] calcSim = new Double[((HashMap) hashMap.get(kl.get(0).getString())).size()];
		int sumKw = 0;
		for(int i = 0; i<kl.size(); i++){
			Keyword kwrd = kl.get(i);
			sumKw += kwrd.getCnt() * kwrd.getCnt();
		}
		Double rootKw = Math.sqrt(sumKw);


		Double[] rootHash = new Double[((HashMap) hashMap.get(kl.get(0).getString())).size()];
		for(int i = 0; i < ((HashMap) hashMap.get(kl.get(0).getString())).size(); i++){
			int sumHash = 0;
			for(int j = 0; j < kl.size(); j++){
				Keyword kwrd = kl.get(j);
				sumHash += (Double) ((HashMap) hashMap.get(kwrd.getString())).get(i);
			}
			sumHash = sumHash * sumHash;
			rootHash[i] = Math.sqrt(sumHash);
		}

		for(int i = 0; i < ((HashMap) hashMap.get(kl.get(0).getString())).size(); i++){
			Double temp = rootKw * rootHash[i];
			calcSim[i] = (double) 0;
			if(temp != 0) {
				calcSim[i] = innerProdcuct[i] / temp;
			}
		}
		return calcSim;
	}


	public void searchXml(){
		System.out.println("5?????? ????????????");

	}
}
