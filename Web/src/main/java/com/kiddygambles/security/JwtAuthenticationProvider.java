package com.kiddygambles.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kiddygambles.domain.principal.JwtUser;
import com.kiddygambles.services.AuthLogic;
import com.kiddygambles.wrappers.LoginWrapper;
import com.kiddygambles.wrappers.TokenWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.kiddygambles.security.SecurityConstants.SecurityConstants.INVENTORY_LOGIN;
import static com.kiddygambles.security.SecurityConstants.SecurityConstants.JWTKEY;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private AuthLogic userDetailsImpl;

    public JwtAuthenticationProvider(AuthLogic userDetailsImpl) {
        this.userDetailsImpl = userDetailsImpl;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtUser user = (JwtUser) userDetails;
        userDetailsImpl.loadUserByID(user.getUserID(), user.getUsername());
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = retrieveAccountData(new LoginWrapper(username, authentication.getCredentials().toString()));

        Claims claims = Jwts.parser()
                .setSigningKey(JWTKEY)
                .parseClaimsJws(token)
                .getBody();

        List<String> scopes = (List<String>) claims.get("scopes");
        int UserId = (int) claims.get("userID");
        List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        return new JwtUser(UserId, username, authentication.getCredentials().toString(), authorities);
    }

    private String retrieveAccountData(LoginWrapper loginWrapper){
        URI uri = UriComponentsBuilder.fromUriString(INVENTORY_LOGIN).build().toUri();
        Gson gson = new GsonBuilder().create();

        RequestEntity<String> request = RequestEntity
                .post(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(gson.toJson(loginWrapper));

        //post call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new UsernameNotFoundException(loginWrapper.getUsername());
        }

        //convert to LoginWrapper
        return gson.fromJson(response.getBody(), TokenWrapper.class).getToken();
    }
}
