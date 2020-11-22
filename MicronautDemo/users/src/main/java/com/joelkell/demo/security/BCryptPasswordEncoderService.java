package com.joelkell.demo.security;

import io.micronaut.security.authentication.providers.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Singleton;

@Singleton
public class BCryptPasswordEncoderService implements PasswordEncoder {

  org.springframework.security.crypto.password.PasswordEncoder delegate =
      new BCryptPasswordEncoder();

  public String encode(String rawPassword) {
    String encoded = delegate.encode(rawPassword);
    return encoded;
  }

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return delegate.matches(rawPassword, encodedPassword);
  }
}
