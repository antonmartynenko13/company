package com.martynenko.anton.company.exception;

import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CrudExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  protected ResponseEntity<Object> handleException(
      RuntimeException ex, WebRequest request) {

    log.info(ex.getClass().getName());

    if (ex instanceof EntityNotFoundException) {
      return handleExceptionInternal(ex, null,
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    if (ex instanceof DataIntegrityViolationException
        //for h2 compatibility
        || ex instanceof InvalidDataAccessApiUsageException) {
      return handleExceptionInternal(ex, null,
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    return handleExceptionInternal(ex, null,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
