package com.example.demo.config;


import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class SecurityFilter extends BasicAuthenticationFilter {

  private final UserDetailsService userDetailsService;
  public SecurityFilter(AuthenticationManager authenticationManager,
                        UserDetailsService userDetailsService) {
    super(authenticationManager);
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    Authentication authentication = getAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  @Override
  protected void onUnsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
    super.onUnsuccessfulAuthentication(request, response, failed);
  }

  private Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null) {
      // Parse the token and extract the username
      String username = Jwts.parser()
          .setSigningKey("YourSecretKey")
          .parseClaimsJws(token.replace("Bearer ", ""))
          .getBody()
          .getSubject();
      UserDetails user = userDetailsService.loadUserByUsername(username);
      if (user != null) {
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
