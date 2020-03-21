package com.rshah.media.mediacontent.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rshah.media.mediacontent.model.ArticleDto;
import com.rshah.media.mediacontent.service.MediaService;

/**
 * The Class MediaController.
 * 
 * @author rshah
 */
@RestController
@CrossOrigin
public class MediaController {

  /** The media service. */
  @Autowired
  MediaService mediaService;

  /**
   * Gets the media content.
   *
   * @param term the term
   * @return the media content
   */
  @GetMapping(path = "/media", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ArticleDto>> getMediaContent(@RequestParam String term) {
    return new ResponseEntity<>(mediaService.getMedia(term), HttpStatus.OK);

  }


}
