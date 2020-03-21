package com.rshah.media.mediacontent.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.rshah.media.mediacontent.client.GoogleClient;
import com.rshah.media.mediacontent.client.ItunesClient;
import com.rshah.media.mediacontent.model.ArticleDto;
import com.rshah.media.mediacontent.model.ResponseDto;

/**
 * The Class MediaService.
 * 
 * It makes parallel calls to itune as well as google apis to get the response
 * 
 * @author rshah
 */
@Service
public class MediaService {

  /** The Constant ERROR_DURING_PARALLEL_CALL. */
  private static final String ERROR_DURING_PARALLEL_CALL = "Error during parallel call";

  /** The logger. */
  Logger logger = LoggerFactory.getLogger(MediaService.class);

  /** The itunes client. */
  @Autowired
  ItunesClient itunesClient;

  /** The google client. */
  @Autowired
  GoogleClient googleClient;

  /** The album. */
  private final String ALBUM = "Album";
  
  /** The book. */
  private final String BOOK = "Book";

  /**
   * Gets the media from itunes and google.
   *
   * @param term the term
   * @return the media
   */
  public List<ArticleDto> getMedia(String term) {
    Future<ResponseDto> albumsResponse = getAlbums(term);
    Future<ResponseDto> booksResponse = getBooks(term);
    return combineResults(serializeAlbums(albumsResponse), serializeBooks(booksResponse));
  }

  /**
   * Async call to get the albums.
   *
   * @param term the term
   * @return the albums
   */
  @Async
  private Future<ResponseDto> getAlbums(String term) {
    String methodName = "getAlbums";
    logger.debug("Inside {} {}", methodName, System.currentTimeMillis());
    return itunesClient.getAlbums(term);
  }

  /**
   * Async call to get the books.
   *
   * @param term the term
   * @return the books
   */
  @Async
  private Future<ResponseDto> getBooks(String term) {
    String methodName = "getBooks";
    logger.debug("Inside {} {}", methodName, System.currentTimeMillis());
    return googleClient.getBooks(term);
  }

  /**
   * Serialize albums.
   *
   * @param futureAlbums the future albums
   * @return the response dto
   */
  private ResponseDto serializeAlbums(Future<ResponseDto> futureAlbums) {
    ResponseDto albumsResponse;
    try {
      albumsResponse = futureAlbums.get();
    } catch (InterruptedException e) {
      logger.error(ERROR_DURING_PARALLEL_CALL, e.getMessage());
      Thread.currentThread().interrupt();
      albumsResponse = null;
    } catch (ExecutionException e) {
      logger.error(ERROR_DURING_PARALLEL_CALL, e.getMessage());
      albumsResponse = null;
    }
    return albumsResponse;
  }

  /**
   * Serialize books.
   *
   * @param futureBooks the future books
   * @return the response dto
   */
  private ResponseDto serializeBooks(Future<ResponseDto> futureBooks) {
    ResponseDto booksResponse;
    try {
      booksResponse = futureBooks.get();
    } catch (InterruptedException e) {
      logger.error(ERROR_DURING_PARALLEL_CALL, e.getMessage());
      Thread.currentThread().interrupt();
      booksResponse = null;
    } catch (ExecutionException e) {
      logger.error(ERROR_DURING_PARALLEL_CALL, e.getMessage());
      booksResponse = null;
    }
    return booksResponse;
  }

  /**
   * Combine results of albums and books.
   *
   * @param albumsResponse the albums response
   * @param booksResponse the books response
   * @return the list
   */
  private List<ArticleDto> combineResults(ResponseDto albumsResponse, ResponseDto booksResponse) {
    List<ArticleDto> articles = new ArrayList<>();
    List<ArticleDto> albums =
        albumsResponse != null && albumsResponse.getResults() != null ? albumsResponse.getResults()
            : Collections.emptyList();
    List<ArticleDto> books = booksResponse != null && booksResponse.getResults() != null ? booksResponse.getResults()
        : Collections.emptyList();
    albums.forEach(album -> album.setMediaType(ALBUM));
    books.forEach(book -> book.setMediaType(BOOK));
    articles.addAll(albums);
    articles.addAll(books);
    Collections.sort(articles);
    return articles;
  }

}
