package com.rshah.media.mediacontent.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class ArticleDto.
 * 
 * @author rshah
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDto implements Comparable<ArticleDto> {

  /** The creator. */
  @JsonAlias("artistName")
  private String creator;

  /** The title. */
  @JsonAlias("trackName")
  private String title;

  /** The media type. */
  private String mediaType;

  /**
   * Extract creator.
   *
   * @param volumeInfo the volume info
   */
  @JsonProperty("volumeInfo")
  private void extractCreator(Map<String, Object> volumeInfo) {
    @SuppressWarnings("unchecked")
    List<String> creatorList = (List<String>) volumeInfo.get("authors");
    this.creator = creatorList == null ? "" : creatorList.stream().collect(Collectors.joining(","));
    this.title = (String) volumeInfo.get("title");
  }

  /**
   * Gets the creator.
   *
   * @return the creator
   */
  public String getCreator() {
    return creator;
  }

  /**
   * Sets the creator.
   *
   * @param creator the new creator
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Gets the title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title.
   *
   * @param title the new title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the media type.
   *
   * @return the media type
   */
  public String getMediaType() {
    return mediaType;
  }

  /**
   * Sets the media type.
   *
   * @param mediaType the new media type
   */
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  @Override
  public int compareTo(ArticleDto articleDto) {
    return this.getTitle() != null && articleDto.getTitle() != null ? this.getTitle().compareTo(articleDto.getTitle())
        : 1;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ArticleDto) {
      ArticleDto articleDto = (ArticleDto) obj;
      return this.getTitle().equals(articleDto.getTitle()) && this.getMediaType().equals(articleDto.getMediaType())
          && getCreator().equals(articleDto.getCreator());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (title == null ? 0 : title.hashCode());
    hash = 31 * hash + (creator == null ? 0 : creator.hashCode());
    hash = 31 * hash + (mediaType == null ? 0 : mediaType.hashCode());
    return hash;
  }


}
