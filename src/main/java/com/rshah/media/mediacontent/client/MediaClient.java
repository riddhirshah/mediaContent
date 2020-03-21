package com.rshah.media.mediacontent.client;

import java.util.Collections;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The Class MediaClient.
 * 
 * @author rshah
 */
@Component
public class MediaClient {

  /** The rest template. */
  @Autowired
  RestTemplate restTemplate;

  /**
   * Generic implementation of get.
   *
   * @param <T> the generic type
   * @param builder the builder
   * @param reference the reference
   * @return the response entity
   */
  public <T> ResponseEntity<T> get(UriComponentsBuilder builder, ParameterizedTypeReference<T> reference) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, reference);
  }

  /**
   * Async get.
   *
   * @param <T> the generic type
   * @param builder the builder
   * @param reference the reference
   * @return the future
   */
  @Async
  public <T> Future<ResponseEntity<T>> asyncGet(UriComponentsBuilder builder, ParameterizedTypeReference<T> reference) {
    return new AsyncResult<ResponseEntity<T>>(get(builder, reference));

  }

}
