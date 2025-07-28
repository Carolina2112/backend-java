package com.javareto.client.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String errorCode = "CLI-001";
        String message = MessageFormat.format(
            messageSource.getMessage("error.message.client.not-found", null, LocaleContextHolder.getLocale()),
            ex.getMessage()
        );
        
        log.warn("Cliente no encontrado - Código: {}, Mensaje: {}, Path: {}", 
                errorCode, message, request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), 
            "Not Found", 
            message, 
            errorCode
        );
        errorResponse.setPath(request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        String errorCode = "CLI-002";
        String message = MessageFormat.format(
            messageSource.getMessage("error.message.client.duplicate", null, LocaleContextHolder.getLocale()),
            ex.getMessage()
        );
        
        log.warn("Cliente duplicado - Código: {}, Mensaje: {}, Path: {}", 
                errorCode, message, request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(), 
            "Conflict", 
            message, 
            errorCode
        );
        errorResponse.setPath(request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Error");
        response.put("errorCode", "VAL-001");
        response.put("message", "Error de validación en los datos de entrada");
        response.put("errors", errors);
        response.put("path", request.getDescription(false));
        response.put("service", "client-person-service");
        
        log.warn("Error de validación - Path: {}, Errores: {}", request.getDescription(false), errors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponse> handleConnectException(ConnectException ex, WebRequest request) {
        String errorCode = "COM-001";
        String message = "Error de conexión con el servicio externo. Por favor, intente más tarde.";
        
        log.error("Error de conexión - Código: {}, Path: {}, Error: {}", 
                errorCode, request.getDescription(false), ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(), 
            "Service Unavailable", 
            message, 
            errorCode
        );
        errorResponse.setPath(request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        String errorCode = "URL-001";
        String message = "El endpoint solicitado no existe: " + ex.getRequestURL();
        
        log.warn("Endpoint no encontrado - Código: {}, Path: {}", 
                errorCode, request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), 
            "Not Found", 
            message, 
            errorCode
        );
        errorResponse.setPath(request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        String errorCode = "SYS-001";
        String message = "Error interno del servidor. Por favor, contacte al administrador.";
        
        log.error("Error interno del servidor - Código: {}, Path: {}, Error: {}", 
                errorCode, request.getDescription(false), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "Internal Server Error", 
            message, 
            errorCode
        );
        errorResponse.setPath(request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}