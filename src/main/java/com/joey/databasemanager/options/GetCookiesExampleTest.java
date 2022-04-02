package com.joey.databasemanager.options;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GetCookiesExampleTest {
	public static void main(String... args) throws IOException, ExecutionException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(
                        URI.create("https://www.nseindia.com/option-chain"))
                .header("accept", "text/html,application/xhtml+xml")
                .build();
        var responseFuture = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        var res = responseFuture.get();
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        for (Map.Entry<String, List<String>> entry : res.headers().map().entrySet()) {
            String key = entry.getKey();
            System.out.println(entry.getValue());
            for (String cookieStr : entry.getValue()) {
                try {
                    for (HttpCookie cookie : HttpCookie.parse(cookieStr)) {
                        System.out.println(cookie.getName() + " ===> " + cookie.getValue());
                    }
                } catch (IllegalArgumentException ignored) {
                    // this string is invalid, jump to the next one.
                } finally {
                    
                }
            }

        }

    }
}
