package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  public static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      // Extract username and password from the request
      String username = request.getParameter("username");
      String password = request.getParameter("password");

      // Create an authentication token
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(username, password);

      // Authenticate the user
      return authenticationManager.authenticate(authenticationToken);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    // Generate JWT token
    String token =
        Jwts.builder()
            .setSubject(authResult.getName())
            .setExpiration(new Date(System.currentTimeMillis() + 864000000)) // 10 days
            .signWith(SignatureAlgorithm.HS512, "YourSecretKey")
            .compact();

    // Add the token to the response header
    response.addHeader("Authorization", "Bearer " + token);
  }
}
