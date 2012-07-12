package ia.datahub.scraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** 
 * Scrapes podracer information from WookieePedia 
 */
public class PodracerScraper {

	/**
	 * Enumerate all podracers available on a well-known WookieePedia page and
	 * pack their attributes into a key/value map
	 */
	public static List<Map<String, String>> scrapePodracers() throws IOException 
	{
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Document doc = Jsoup.connect("http://starwars.wikia.com/wiki/Podracer").get();
		Elements uls = doc.select("h3 + ul, figure + ul");
		for (Element e : uls)
		{
			Elements items = e.children();
			for (Element i : items)
			{
				Elements anchors = i.select("a");
				if (anchors.size() > 0)
				{
					String href = anchors.get(0).attr("href");
					try {
						Map<String, String> attributes = parsePodracer(href);
						result.add(attributes);
					} catch (IllegalArgumentException iae)
					{
						// Do nothing - spurious link?
					}
				}	
			}
		}
		return result;
	}

	private static Map<String, String> parsePodracer(String href) throws IOException 
	{
		Map<String, String> attributes = new HashMap<String, String>();
		Document doc = Jsoup.connect("http://starwars.wikia.com" + href).get();
		Elements infoboxes = doc.select("div#Repulsorlift_vehicle_infobox");
		if (infoboxes.size() != 1)
		{
			throw new IllegalArgumentException();
		}
		Element infobox = infoboxes.get(0);
		Elements heading = infobox.select("th.infoboxheading");
		if (heading.size() > 0)
		{
			attributes.put("Model", heading.get(0).text());
		}
		Elements rows = infobox.select("tr");
		for (Element row : rows)
		{
			Elements data = row.select("td");
			if (data.size() == 2)
			{
				attributes.put(data.get(0).text(), data.get(1).text());
			}
		}
		return attributes;
	}

	// Helper entry point for debugging
	public static void main(String[] args) throws IOException
	{
		List<Map<String, String>> results = scrapePodracers();
		for (Map<String, String> podracer : results)
		{
			System.out.println(podracer.get("Model"));
			for (Entry<String, String> e : podracer.entrySet())
			{
				System.out.println("    " + e.getKey() + ":" + e.getValue());
			}
		}
	}
}
