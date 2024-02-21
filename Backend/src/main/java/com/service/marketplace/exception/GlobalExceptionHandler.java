package com.service.marketplace.exception;

import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.api.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    //UserServiceImpl
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Entity not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>("Invalid argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        // You might want to log the exception or perform some other action here.
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        // Custom handling for user not found
        return new ResponseEntity<>("User not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> handleFileUploadException(FileUploadException ex, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException ex) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Subscription Controller
    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<Object> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //Stripe Service Impl
    @ExceptionHandler(PayloadProcessingException.class)
    public ResponseEntity<Object> handlePayloadProcessingException(PayloadProcessingException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeAccountCreationException.class)
    public ResponseEntity<Object> handleStripeAccountCreationException(StripeAccountCreationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeSessionCreationException.class)
    public ResponseEntity<Map<String, Object>> handleStripeSessionCreationException(StripeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeServiceException.class)
    public ResponseEntity<Map<String, Object>> handleStripeServiceException(StripeServiceException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getErrorCode());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    //Storage Service Impl
    @ExceptionHandler(FileConversionException.class)
    public ResponseEntity<?> handleFileConversionException(FileConversionException ex, WebRequest request) {
        return new ResponseEntity<>("File conversion error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Service Impl
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<?> handleServiceNotFoundException(ServiceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Service not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //Review Service Impl
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<?> handleReviewNotFoundException(ReviewNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Review not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //Request Service Impl
    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<?> handleRequestNotFoundException(RequestNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Request not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //Offer Service Impl
    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<?> handleOfferNotFoundException(OfferNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Offer not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // File Service Impl
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("File not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //City Service Impl
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<?> handleCityNotFoundException(CityNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("City not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}











