package com.task.urlshortner.controller;

import com.task.urlshortner.exception.BadRequest;
import com.task.urlshortner.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("url")
public class UrlController {

    private final String GENERATE_ANOTHER_LINK = "<p><a href='http://localhost:8080/index.html'>Try another URL</a></p>";

    @Autowired
    private UrlService urlService;

    @GetMapping("shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam("url") String url) throws BadRequest {
        String shortenedUrl = urlService.shortenUrl(url);
        String response = "<p>Generated short URL: <a href='" + shortenedUrl + "'>" + shortenedUrl +"</a></p>";
        return ResponseEntity.ok(response + GENERATE_ANOTHER_LINK);
    }

    @GetMapping("{uuid}")
    public RedirectView getUrl(@PathVariable("uuid") String uuid) throws BadRequest {
            return new RedirectView(urlService.getOriginalUrl(uuid));
    }

    @ExceptionHandler({ BadRequest.class })
    public ResponseEntity<String> handleException(BadRequest e) {
        return ResponseEntity.badRequest().body(e.getMessage() + GENERATE_ANOTHER_LINK);
    }
}
