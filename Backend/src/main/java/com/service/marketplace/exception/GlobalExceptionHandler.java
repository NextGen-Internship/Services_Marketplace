package com.service.marketplace.exception;

import com.service.marketplace.dto.response.CustomErrorResponse;
import com.stripe.exception.SignatureVerificationException;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    //UserServiceImpl
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Entity not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Invalid argument", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Runtime error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("User not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<CustomErrorResponse> handleFileUploadException(FileUploadException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("File upload error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex) {
        CustomErrorResponse error = new CustomErrorResponse("Role not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Subscription Controller
    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Subscription not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Stripe Service Impl
    @ExceptionHandler(PayloadProcessingException.class)
    public ResponseEntity<CustomErrorResponse> handlePayloadProcessingException(PayloadProcessingException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Payload processing error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeAccountCreationException.class)
    public ResponseEntity<CustomErrorResponse> handleStripeAccountCreationException(StripeAccountCreationException ex) {
        CustomErrorResponse error = new CustomErrorResponse("Stripe account creation error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeSessionCreationException.class)
    public ResponseEntity<CustomErrorResponse> handleStripeSessionCreationException(StripeException ex) {
        CustomErrorResponse error = new CustomErrorResponse("Stripe session creation error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeServiceException.class)
    public ResponseEntity<CustomErrorResponse> handleStripeServiceException(StripeServiceException ex) {
        CustomErrorResponse error = new CustomErrorResponse("Stripe service error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Storage Service Impl
    @ExceptionHandler(FileConversionException.class)
    public ResponseEntity<CustomErrorResponse> handleFileConversionException(FileConversionException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("File conversion error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Service Impl
    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleServiceNotFoundException(ServiceNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Service not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Review Service Impl
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleReviewNotFoundException(ReviewNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Review not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Request Service Impl
    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleRequestNotFoundException(RequestNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Request not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Offer Service Impl
    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleOfferNotFoundException(OfferNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Offer not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // File Service Impl
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("File not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //City Service Impl
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleCityNotFoundException(CityNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("City not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Category Service Impl
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Category not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //VipService
    @ExceptionHandler(VipServiceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleVipServiceNotFoundException(VipServiceNotFoundException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("VipService not found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Storage Service Impl
    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<CustomErrorResponse> handleMalformedURLException(MalformedURLException ex, WebRequest request) {
        CustomErrorResponse error = new CustomErrorResponse("Malformed URL", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomErrorResponse> handleIOException(IOException ex) {
        CustomErrorResponse error = new CustomErrorResponse("I/O Error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
        CustomErrorResponse error = new CustomErrorResponse("Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<CustomErrorResponse> handleSignatureVerificationException(SignatureVerificationException ex) {
        CustomErrorResponse error = new CustomErrorResponse("Unauthorized", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(FileConversionException.class)
//    public ResponseEntity<Object> handleFileConversionException(FileConversionException ex) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        body.put("error", "Internal Server Error");
//        body.put("message", "Failed to convert file: " + ex.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(GeneralSecurityException.class)
    public ResponseEntity<CustomErrorResponse> handleGeneralSecurityException(GeneralSecurityException ex) {
        CustomErrorResponse error = new CustomErrorResponse("Security Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }


}











