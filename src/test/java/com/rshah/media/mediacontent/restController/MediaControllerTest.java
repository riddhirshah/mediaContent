package com.rshah.media.mediacontent.restController;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rshah.media.mediacontent.model.ArticleDto;
import com.rshah.media.mediacontent.service.MediaService;

@ExtendWith(MockitoExtension.class)
public class MediaControllerTest {
  
  @InjectMocks
  MediaController mediaController;
   
  @Mock
  MediaService mediaService;
  
  @Test
  public void getMediaContentTest() 
  {
      // given


      Mockito.when(mediaService.getMedia("Love")).thenReturn(getArticles());

      ResponseEntity<List<ArticleDto>> entity=mediaController.getMediaContent("Love");
      Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
      Assertions.assertEquals(3, entity.getBody().size());

  }
  
  private List<ArticleDto> getArticles() {
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
    articleDto.setCreator("Amish Tripathi");
    articleDto.setMediaType("book");
    articleDto.setTitle("Secret of Nagas-Love");
    articleList.add(articleDto);
    return articleList;
  }


}
