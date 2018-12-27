package com.kiddygambles.services.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.AUTHHEADER;

@Service
public class RestCallHelper {
    private HttpServletRequest request;

    @Autowired
    public RestCallHelper(HttpServletRequest request) {
        this.request = request;
    }

    public ResponseEntity<String> makePostRestCall(String url, String data) {
        //Use build in spring uri builder to avoid urisyntaxexception handling
        URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        String token = request.getHeader(AUTHHEADER);

        RequestEntity<String> request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHHEADER, token)
                .body(data);

        RestTemplate restCall = new RestTemplate();
        ResponseEntity<String> response = restCall.exchange(request, String.class);

        return response;
    }

    public <T> ResponseEntity<T> makeGetRestCall(String url, Class<T> type){
        //set the requested headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHHEADER, request.getHeader(AUTHHEADER));

        //make a empty entity
        HttpEntity<?> httpEntity = new HttpEntity<>("", headers);

        //get call
        RestTemplate restCall = new RestTemplate();
        ResponseEntity<T> response = restCall.exchange(url, HttpMethod.GET, httpEntity, type);

        return response;
    }
}
