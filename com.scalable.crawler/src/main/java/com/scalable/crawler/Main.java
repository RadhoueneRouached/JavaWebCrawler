package com.scalable.crawler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static java.util.stream.Collectors.toMap;

/**
 * WebCrawler counts top Javascript libraries used in
 * web pages found on Google.
 */
public class Main {

    public static <K, V> void print(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Library %d: " + entry.getKey()
                    + " Counts : " + entry.getValue());
        }
    }

    public static void main(String args[]) {
        // Instantiate Crawler
        Crawler crawler = new Crawler();
        // Read string input
        Scanner inputs = new Scanner(System.in);
        System.out.println("Please, enter the search term: ");
        String searchTerm = inputs.nextLine();
        // Extract JS libraries
        Map<String, Integer> libraries = crawler.crawl(searchTerm);
        // Sort and limit Libraries
        Map<String, Integer> sorted = libraries.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(5).collect(
                toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                        LinkedHashMap::new));
        // Display final result
        print(sorted);
    }
}
