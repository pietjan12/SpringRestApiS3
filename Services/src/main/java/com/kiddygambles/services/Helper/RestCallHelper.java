package com.kiddygambles.services.Helper;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class RestCallHelper {
    public ResponseEntity<String> makePostRestCall(String url, String token, String data) {
        //Use build in spring uri builder to avoid urisyntaxexception handling
        URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        RequestEntity<String> request = RequestEntity
                .post(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",token)
                .body(data);

        RestTemplate restCall = new RestTemplate();
        return restCall.exchange(request, String.class);
    }

    //TODO TESTEN OF DEZE GENERIC METHOD WERKT.
    public <T> ResponseEntity<T> makeGetRestCall(String url, String token, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<?> httpEntity = new HttpEntity<>("" , headers);

        RestTemplate restcall = new RestTemplate();
        return restcall.exchange(url, HttpMethod.GET, httpEntity, type);
    }
}
