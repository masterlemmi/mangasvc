package com.lemoncode.util;

import org.springframework.stereotype.Service;

@Service
public class UrlParser {


    public String getLastString(String url) {
        String trimmed = url.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.replaceAll("/$", "");
        }
        return trimmed.substring(trimmed.lastIndexOf("/") + 1);
    }

    public static void main(String[] args) {

    }

}
