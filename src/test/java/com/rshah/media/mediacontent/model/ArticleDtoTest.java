package com.rshah.media.mediacontent.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rshah.media.mediacontent.model.ResponseDto;

@ExtendWith(MockitoExtension.class)
public class ArticleDtoTest {


  @Test
  void getMethodTest() throws JsonMappingException, JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String json =
        "{\"kind\":\"books#volumes\",\"totalItems\":742,\"items\":[{\"volumeInfo\":{\"title\":\"Sex, Love, and Friendship\",\"authors\":[\"Alan Soble\"]}}]}";
    ResponseDto response = objectMapper.readValue(json, ResponseDto.class);
    Assertions.assertEquals(1, response.getResults().size());
  }

  
  @Test
  void getMultipleAuthorTest() throws JsonMappingException, JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String json="{\"kind\":\"books#volumes\",\"totalItems\":742,\"items\":[{\"volumeInfo\":{\"title\":\"Sex, Love, and Friendship\",\"authors\":[\"Alan Soble\",\"Riddhi\"]}}]}";
    ResponseDto response = objectMapper.readValue(json, ResponseDto.class);
    Assertions.assertEquals(1, response.getResults().size());
  }
  
  @Test
  void getItuneTest() throws JsonMappingException, JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String json="{\"resultCount\":1,\"results\":[{\"artistName\":\"Nat King Cole\",\"trackName\":\"L-O-V-E\"}]}";
    ResponseDto response = objectMapper.readValue(json, ResponseDto.class);
    Assertions.assertEquals(1, response.getResults().size());
  }


}
