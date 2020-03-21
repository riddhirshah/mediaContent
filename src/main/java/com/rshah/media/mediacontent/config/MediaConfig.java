package com.rshah.media.mediacontent.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rshah.media.mediacontent.exception.MediaAsyncExceptionHandler;

/**
 * The Class MediaConfig.
 * 
 * @author rshah
 */
@EnableWebMvc
@Configuration
@EnableAsync
@SpringBootApplication
@ComponentScan("com.rshah")
public class MediaConfig implements WebMvcConfigurer, AsyncConfigurer {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(MediaConfig.class, args);
  }

  /** The read timeout. */
  @Value("${service.client.timeout.read}")
  private Duration readTimeout;

  /** The connect timeout. */
  @Value("${service.client.timeout.connection}")
  private Duration connectTimeout;

  /**
   * Async executor.
   *
   * @return the executor
   */
  @Bean
  public Executor asyncExecutor() {
    return new ThreadPoolTaskExecutor();
  }

  /**
   * Rest template.
   *
   * @param restTemplateBuilder the rest template builder
   * @return the rest template
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    RestTemplate restTemplate =
        restTemplateBuilder.setReadTimeout(readTimeout).setConnectTimeout(connectTimeout).build();
    List<HttpMessageConverter<?>> converters = new ArrayList<>();
    converters.add(jsonConverter());
    restTemplate.setMessageConverters(converters);
    return restTemplate;
  }

  /**
   * Configure message converters.
   *
   * @param converters the converters
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jsonConverter());
    converters.add(stringConverter());
    converters.add(resourceConverter());
  }

  /**
   * Json converter.
   *
   * @return the mapping jackson 2 http message converter
   */
  private MappingJackson2HttpMessageConverter jsonConverter() {
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    List<MediaType> mTypes = new LinkedList<>(jsonConverter.getSupportedMediaTypes());
    mTypes.add(new MediaType("application", "json", StandardCharsets.UTF_8));
    mTypes.add(new MediaType("text", "javascript", StandardCharsets.UTF_8));
    jsonConverter.setSupportedMediaTypes(mTypes);
    return jsonConverter;
  }

  /**
   * String converter.
   *
   * @return the string http message converter
   */
  private StringHttpMessageConverter stringConverter() {
    StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
    List<MediaType> supportedMediaTypes = new ArrayList<>();
    supportedMediaTypes.add(MediaType.TEXT_HTML);
    supportedMediaTypes.add(MediaType.ALL);
    stringConverter.setSupportedMediaTypes(supportedMediaTypes);
    return stringConverter;
  }

  /**
   * Resource converter.
   *
   * @return the resource http message converter
   */
  private ResourceHttpMessageConverter resourceConverter() {
    return new ResourceHttpMessageConverter();
  }

  /**
   * Gets the async uncaught exception handler.
   *
   * @return the async uncaught exception handler
   */
  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new MediaAsyncExceptionHandler();
  }
  
}
