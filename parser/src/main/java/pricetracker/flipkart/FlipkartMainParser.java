package pricetracker.flipkart;

import java.io.IOException;  
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;  

public class FlipkartMainParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://www.flipkart.com/all/apple~brand/pr?sid=all";
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			String title = doc.title();  
			System.out.println(title);
			String class_name = "_1YokD2 _3Mn1Gg";
			Elements els = doc.getElementsByClass(class_name);
			System.out.println(els);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
			
	}

}
