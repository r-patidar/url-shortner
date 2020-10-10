package com.task.urlshortner.service;

import com.task.urlshortner.exception.BadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Service
public class UrlServiceImpl implements UrlService {

    @Value("${base-url}")
    private String baseUrl;

    private static final Map<Integer, String> urlMap = new HashMap<>();

    private static Integer currentCount = 1;

    private static final char[] ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    @Override
    public String shortenUrl(String url) throws BadRequest {
        if(url == null || url.isEmpty()) {
            throw new BadRequest("url parameter cannot be empty.");
        }
        if(isValidUrl(url)) {
            Integer urlIdentifier = currentCount++;
            urlMap.put(urlIdentifier, url);
            return UriComponentsBuilder.fromHttpUrl(baseUrl).path(getShortUrl(urlIdentifier)).toUriString();
        } else {
            throw new BadRequest("Provided url parameter is not a valid url.");
        }
    }

    @Override
    public String getOriginalUrl(String shortUrl) throws BadRequest {
        if(shortUrl == null || shortUrl.isEmpty()) {
            throw new BadRequest("provided short url is not valid.");
        }
        int urlId = stringtoUrlId(shortUrl);
        if(!urlMap.containsKey(urlId)) {
            throw new BadRequest("requested url does not exist");
        }
        return urlMap.get(stringtoUrlId(shortUrl));
    }

    /**
     * convert a number to base 62 string
     * @param n
     * @return
     */
    private String getShortUrl(Integer n) {
        StringBuffer shorturl = new StringBuffer();
        while (n > 0) {
            shorturl.append(ALLOWED_CHARACTERS[n % 62]);
            n = n / 62;
        }
        return shorturl.reverse().toString();
    }

    /**
     * get the url id from short string
     * @param string
     * @return
     */
    private int stringtoUrlId(String string) {
        int id = 0;
        for (int i = 0; i < string.length(); i++) {
            if ('a' <= string.charAt(i) && string.charAt(i) <= 'z')
                id = id * 62 + string.charAt(i) - 'a';
            else if ('A' <= string.charAt(i) && string.charAt(i) <= 'Z')
                id = id * 62 + string.charAt(i) - 'A' + 26;
            else if ('0' <= string.charAt(i) && string.charAt(i) <= '9')
                id = id * 62 + string.charAt(i) - '0' + 52;
        }
        return id;
    }


    /**
     * check if provided url is valid or not
     * @param url
     * @return
     * @throws BadRequest
     */
    private boolean isValidUrl(String url) throws BadRequest {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
