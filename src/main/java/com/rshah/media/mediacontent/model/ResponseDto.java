package com.rshah.media.mediacontent.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class ResponseDto.
 * 
 * @author rshah
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {

    /** The results. */
    @JsonProperty("items")
    @JsonAlias("results")
	List<ArticleDto> results;

	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public List<ArticleDto> getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results the new results
	 */
	public void setResults(List<ArticleDto> results) {
		this.results = results;
	}
}
