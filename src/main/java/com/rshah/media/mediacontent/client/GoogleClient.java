package com.rshah.media.mediacontent.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import com.rshah.media.mediacontent.model.ResponseDto;

/**
 * The Class GoogleClient.
 * 
 * @author rshah
 */
@Component
public class GoogleClient extends MediaClient {

  /** The limit. */
  @Value("${media.limit}")
  private int limit;

  /** The google url. */
  @Value("${google.url}")
  private String googleUrl;

  /**
   * Gets the books from googleapi.
   *
   * @param term the term
   * @return the books
   */
  @Async
  public Future<ResponseDto> getBooks(String term) {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(googleUrl).queryParam("q", term).queryParam("maxResults", limit);
    Future<ResponseEntity<ResponseDto>> future =
        this.asyncGet(builder, new ParameterizedTypeReference<ResponseDto>() {});
    ResponseEntity<ResponseDto> responseEntity = null;
    try {
      responseEntity = future.get();
    } catch (InterruptedException e) {
      HttpStatus status =
          (e.getCause() instanceof HttpStatusCodeException) ? ((HttpStatusCodeException) e.getCause()).getStatusCode()
              : HttpStatus.INTERNAL_SERVER_ERROR;
      responseEntity = new ResponseEntity<>(status);
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new AsyncResult<>(responseEntity.getBody());
  }

}
