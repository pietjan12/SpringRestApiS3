package com.kiddygambles.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiddygambles.domain.principal.JwtUser;
import com.kiddygambles.wrappers.LoginWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.Json;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.kiddygambles.security.SecurityConstants.SecurityConstants.JWTKEY;
import static com.kiddygambles.services.Constants.KiddyAPIConstants.inventoryURL;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try{
            LoginWrapper creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginWrapper.class);

            //Get inventory account credentials, and if they do not exist yet, create them for the user.
            getInventoryAccount(creds.getUsername(), creds.getPassword());

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        //generate jwt token with issued date and expiration date of issued date + 1
        Date expirationDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date currentDate = Date.valueOf(LocalDate.now());

        JwtUser user = (JwtUser)auth.getPrincipal();
        //get username and make a claim with roles
        String subject = user.getUsername();
        Claims claim = Jwts.claims().setSubject(subject);
        claim.put("scopes", auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
        claim.put("userID", user.getUserID());

        //build token
        String token =  Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS512, JWTKEY)
                .compact();

        //Convert token to json and return to the user
        JSONObject tokenResponse = new JSONObject();
        try {
            tokenResponse.put("token",  token);
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }

        //Write token response to body
        res.getWriter().print(tokenResponse);
    }

    /**
     * Login to inventory Module cause we are dependent on it, if an account is not yet made for them make one for them. Also returns an error if the data is not correct.
     * @param username the username of the attempted login
     * @param password the password of the attempted login
     */
    private void getInventoryAccount(String username, String password) {
        URI uri = UriComponentsBuilder.fromUriString(inventoryURL + "/login")
                .build()
                .toUri();

        String jsonData = Json.createObjectBuilder()
                .add("username", username)
                .add("password", password)
                .build().toString();

        RequestEntity<String> request = RequestEntity
                .post(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(jsonData);

        RestTemplate restCall = new RestTemplate();
        ResponseEntity<String> response = restCall.exchange(request, String.class);

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Something went terribly wrong on our end, please contact support!");
        }
    }
}
