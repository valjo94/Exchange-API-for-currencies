package com.currency.foreignexchange.exceptionhandling;

import com.currency.foreignexchange.exceptionhandling.custom.CurrencyCodeException;
import com.currency.foreignexchange.exceptionhandling.custom.ElementNotFoundException;
import com.currency.foreignexchange.exceptionhandling.custom.IncorrectDateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.ServiceUnavailableException;
import javax.validation.ConstraintViolationException;

/**
 * The GlobalExceptionHandler is used to intercept all the exceptions thrown by the application and return appropriate responses
 * with appropriate http status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArguments(IllegalArgumentException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage("Request contains an invalid argument.");
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ElementNotFoundException.class})
    protected ResponseEntity<Object> handleElementNotFoundException(ElementNotFoundException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IncorrectDateException.class, CurrencyCodeException.class})
    protected ResponseEntity<Object> handleConstraints(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getCause().getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleInvalidParameters(ConstraintViolationException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getConstraintViolations().iterator().next().getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ServiceUnavailableException.class})
    protected ResponseEntity<Object> handleThirdPartyServiceUnavailable(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }


}


