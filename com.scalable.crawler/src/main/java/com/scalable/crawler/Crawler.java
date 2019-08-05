package com.scalable.crawler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crawler {

    private static final Logger LOGGER = Logger.getLogger(Crawler.class.getName());

    protected Map<String, Integer> crawl(String searchTerm) {
        try {
            String url = String.format("https://www.google.com/search?q=%s", StringUtils.replace(searchTerm, " ", "+"));
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select(".r a");
            Map<String, Integer> libraries = new HashMap<>();
            for (Element link : links) {
                String href = link.absUrl("href");
                System.out.println("Loading: " + href);
                try {
                    Document page = Jsoup.connect(href).get();
                    Elements scripts = page.select("script");
                    for (Element script : scripts) {
                        String source = script.absUrl("src");
                        if (!source.isEmpty()) {
                            URL webPage = new URL(source);
                            String library = webPage.getPath().substring(webPage.getPath().lastIndexOf("/") + 1);
                            libraries.merge(library, 1, (old, one) -> old + one);
                        }
                    }
                } catch (MalformedURLException e) {
                    LOGGER.log(Level.SEVERE, "Malformed URL " + href);
                } catch (HttpStatusException e) {
                    LOGGER.log(Level.SEVERE, "Could not fetch page " + href);
                }
            }
            return libraries;
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage());
        }
        return null;
    }
}
