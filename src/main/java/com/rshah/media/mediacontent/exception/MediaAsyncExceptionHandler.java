package com.rshah.media.mediacontent.exception;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

/**
 * The Class MediaAsyncExceptionHandler.
 * 
 * @author rshah
 */
public class MediaAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  /** The logger. */
  Logger logger = LoggerFactory.getLogger(MediaAsyncExceptionHandler.class);

  /**
   * Handle uncaught exception.
   *
   * @param throwable the throwable
   * @param method the method
   * @param obj the obj
   */
  @Override
  public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
    logger.error("Exception during async call - {} {}", throwable.getMessage(), method);
  }
}
