package com.rshah.media.mediacontent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.AsyncResult;

import com.rshah.media.mediacontent.client.GoogleClient;
import com.rshah.media.mediacontent.client.ItunesClient;
import com.rshah.media.mediacontent.model.ArticleDto;
import com.rshah.media.mediacontent.model.ResponseDto;

@ExtendWith(MockitoExtension.class)

public class MediaServiceTest {

  @InjectMocks
  MediaService mediaService;

  @Mock
  ItunesClient ituneClient;

  @Mock
  GoogleClient googleClient;

  @Test
  void getMediaPositive() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneRespose());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getGoogleResponse());
    Assertions.assertEquals(2, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaErrorFromOneApi() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneError());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getGoogleResponse());
    Assertions.assertEquals(1, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaErrorFromBothApi() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneError());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getGoogleError());
    Assertions.assertEquals(0, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaErrorFromGoogleApi() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneRespose());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getGoogleError());
    Assertions.assertEquals(1, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaOnlyFromItune() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneMorethanOneRespose());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getZeroResponseFromGoogle());
    Assertions.assertEquals(2, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaOnlyFromGoogleandNullFromGoogle() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getNullResultsFromItunes());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getGoogleResponse());
    Assertions.assertEquals(1, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaOnlyFromItuneandNullFromApple() {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneMorethanOneRespose());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getNullResultsFromGoogle());
    Assertions.assertEquals(2, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaFromGoogleExecutionException() throws InterruptedException, ExecutionException {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(getItuneMorethanOneRespose());
    Mockito.when(googleClient.getBooks("Love")).thenReturn(CompletableFuture.supplyAsync(() -> {
      System.out.println("running task");
      int i = 10 / 0; // For throwing ExecutionException
      return new ResponseDto();
    }));
    Assertions.assertEquals(2, mediaService.getMedia("Love").size());
  }

  @Test
  void getMediaFromItuneExecutionException() throws InterruptedException, ExecutionException {
    Mockito.when(ituneClient.getAlbums("Love")).thenReturn(CompletableFuture.supplyAsync(() -> {
      System.out.println("running task");
      int i = 10 / 0; // For throwing ExecutionException
      return new ResponseDto();
    }));
    Mockito.when(googleClient.getBooks("Love")).thenReturn(getGoogleResponse());
    Assertions.assertEquals(1, mediaService.getMedia("Love").size());
  }
  
  private Future<ResponseDto> getNullResultsFromItunes() {
    ResponseDto response = new ResponseDto();
    List<ArticleDto> bookList = null;
    response.setResults(bookList);
    return new AsyncResult<ResponseDto>(response);
  }

  private Future<ResponseDto> getNullResultsFromGoogle() {
    ResponseDto response = new ResponseDto();
    List<ArticleDto> bookList = null;
    response.setResults(bookList);
    return new AsyncResult<ResponseDto>(response);
  }

  private Future<ResponseDto> getZeroResponseFromGoogle() {
    ResponseDto response = new ResponseDto();
    List<ArticleDto> bookList = new ArrayList<ArticleDto>();
    response.setResults(bookList);
    return new AsyncResult<ResponseDto>(response);
  }

  private Future<ResponseDto> getGoogleResponse() {
    ResponseDto response = new ResponseDto();
    ArticleDto bookDto = new ArticleDto();
    List<ArticleDto> bookList = new ArrayList<ArticleDto>();
    bookDto.setCreator("Amish Tripathi");
    bookDto.setMediaType("book");
    bookDto.setTitle("Secret of Nagas-Love");
    bookList.add(bookDto);
    response.setResults(bookList);
    return new AsyncResult<ResponseDto>(response);
  }



  private Future<ResponseDto> getItuneRespose() {
    ResponseDto response = new ResponseDto();
    ArticleDto articleDto = new ArticleDto();
    List<ArticleDto> articleList = new ArrayList<ArticleDto>();
    articleDto.setCreator("Kishore Kumar");
    articleDto.setMediaType("album");
    articleDto.setTitle("Aa gale lag ja-Love");
    articleList.add(articleDto);
    response.setResults(articleList);
    return new AsyncResult<ResponseDto>(response);
  }

  private Future<ResponseDto> getItuneError() {
    return new AsyncResult<ResponseDto>(null);
  }

  private Future<ResponseDto> getGoogleError() {
    return new AsyncResult<ResponseDto>(null);
  }

  private Future<ResponseDto> getItuneMorethanOneRespose() {
    ResponseDto response = new ResponseDto();
    ArticleDto articleDto = new ArticleDto();
    List<ArticleDto> articleList = new ArrayList<ArticleDto>();
    articleDto.setCreator("Kishore Kumar");
    articleDto.setMediaType("album");
    articleDto.setTitle("Aa gale lag ja-Love");
    articleList.add(articleDto);
    articleDto.setCreator("Lata Mangeshkar");
    articleDto.setMediaType("album");
    articleDto.setTitle("Lag ja gale-Love");
    articleList.add(articleDto);
    response.setResults(articleList);
    return new AsyncResult<ResponseDto>(response);
  }

}
