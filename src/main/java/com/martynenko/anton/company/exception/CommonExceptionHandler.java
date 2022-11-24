package com.martynenko.anton.company.exception;

import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
  public static final String CONFLICT_MESSAGE = "The request cannot be completed due to duplicate unique fields or other inconsistencies.";

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
   // return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    return new ResponseEntity<>( CONFLICT_MESSAGE, HttpStatus.CONFLICT);
  }

  //for h2 compatibility
  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  //@ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<String> handleInvalidDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
    return new ResponseEntity<>(CONFLICT_MESSAGE, HttpStatus.CONFLICT);
  }

  /*@ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }*/

  /*@ExceptionHandler
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
          new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    return handleExceptionInternal(ex, null,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }*/
}