package Hutechlibrary.Anu.Library.exception;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.DataResponse;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
	        DataResponse dataResponse = new DataResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(dataResponse));
	    }

	    @ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<ApiResponse> handleAccessDenied() {
	        DataResponse dataResponse = new DataResponse(HttpStatus.FORBIDDEN.value(), "Access Denied", null);
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(dataResponse));
	    }

	    @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<ApiResponse> handleAuthException() {
	        DataResponse dataResponse = new DataResponse(HttpStatus.UNAUTHORIZED.value(), "Authentication Failed", null);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(dataResponse));
	    }

	    // Optional: fallback handler
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
	        DataResponse dataResponse = new DataResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(dataResponse));
	    }
	    
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();
	        ex.getBindingResult().getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage())
	        );

	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
	}
