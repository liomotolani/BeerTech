package com.beerkhaton.mealtrackerapi.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beerkhaton.mealtrackerapi.model.User;
import com.beerkhaton.mealtrackerapi.repository.UserRepository;
import com.beerkhaton.mealtrackerapi.util.Constants;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
@Data
@RequiredArgsConstructor
public class TokenProvider {
    private final UserRepository userRepository;

    @Value("jjwt.secret.key")
    private String secret;

    @Value("jjwt.expiration")
    private String expirationTime;

    private Key key;

    private Long id;

    private String email;

    private String name;

    private String token;

    private String role;


    void setDetails(String token) {
        DecodedJWT claims = JWT.decode(token);
        id = claims.getClaim("userId").asLong();
        this.email = claims.getClaim("email").asString();
        this.name = claims.getClaim("name").asString();
        this.role = claims.getClaim("role").asString();
        this.token = token;

    }


    public String getUsernameFromJWTToken(String token) {
        return getClaimFromJWTToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromJWTToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJWTToken(token);
        return claimsResolver.apply(claims);
    }


    public Claims getAllClaimsFromJWTToken(String token) {
        return Jwts.parser()
                .setSigningKey(Constants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateJWTToken(Authentication authentication) {
        User user = userRepository.findByName(authentication.getName()).get();

        String jwts=  Jwts.builder()
                .setSubject(authentication.getName())
                .claim("userId",user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role",user.getRole().name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .compact();

        return jwts;
    }

    public Boolean validateJWTToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWTToken(token);
        return (username.equals(userDetails.getUsername()));
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, Authentication existingAuth, UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(Constants.SIGNING_KEY);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(""))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}
