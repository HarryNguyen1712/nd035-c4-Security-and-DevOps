package com.example.demo.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

  public static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException e) {
    log.error(e.getMessage());
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
