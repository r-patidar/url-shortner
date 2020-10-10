package com.task.urlshortner.service;

import com.task.urlshortner.exception.BadRequest;

public interface UrlService {

    /**
     * get the short url representation of the given request url
     * @param url url to be shorted
     * @return shorted url
     * @throws BadRequest if the provided request is not valid
     */
    String shortenUrl(String url) throws BadRequest;

    /**
     * convet the previously shorted url to original url
     * @param uuid short url key
     * @return original url
     * @throws BadRequest if the short url is not valid
     */
    String getOriginalUrl(String uuid) throws BadRequest;
}
