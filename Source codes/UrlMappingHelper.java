package com.example.app;

import java.util.HashMap;
import java.util.Map;

public class UrlMappingHelper {

    private static final Map<String, String> URL_MAP = new HashMap<>();

    // Static Initializer Block
    static {
        URL_MAP.put("123", "https://drive.google.com/file/d/1A0wlhjWGG1DLd8-rLI0FIYM6OYnhQjyr/preview");
        URL_MAP.put("456", "file:///android_asset/YouTube.html");
        URL_MAP.put("789", "file:///android_asset/example_com.html");
    }

    // Private constructor to prevent instantiation
    private UrlMappingHelper() {}

    public static String getUrl(String key) {
        return URL_MAP.get(key);
    }
}
