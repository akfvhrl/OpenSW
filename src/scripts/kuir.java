package scripts;

public class kuir {
	public static void main(String[] args) throws Exception {
		
		String command = args[0];   
		String path = args[1];

		if(command.equals("-c")) {
			makeCollection collection = new makeCollection(path);
			collection.makeXml();
		}
		else if(command.equals("-k")) {
			makeKeyword keyword = new makeKeyword(path);
			keyword.convertXml();
		}
		else if(command.equals("-i")) {
			indexer index = new indexer(path);
			index.indexXml();
		}
		else if(command.equals("-s")) {

			searcher search = new searcher(path, args[3]);
			search.searchXml();

		}
		else if(command.equals("-m")){

			MidTerm showSnippet = new MidTerm();
			showSnippet.showSnippet(path, args[3]);
		}
	}
}
