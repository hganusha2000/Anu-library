package Hutechlibrary.Anu.Library.exception;

import java.time.LocalDateTime;
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
import Hutechlibrary.Anu.Library.dto.ApiResponse1;
import Hutechlibrary.Anu.Library.dto.DataResponse;

@ControllerAdvice
@RestControllerAdvice
	public class GlobalExceptionHandler {

	    @ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<ApiResponse1> handleAccessDenied() {
	        DataResponse dataResponse = new DataResponse(
	            HttpStatus.FORBIDDEN.value(),
	            "Access Denied",
	            null
	        );
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse1(dataResponse));
	    }

	    @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<ApiResponse1> handleAuthException() {
	        DataResponse dataResponse = new DataResponse(
	            HttpStatus.UNAUTHORIZED.value(),
	            "Authentication Failed",
	            null
	        );
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse1(dataResponse));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiResponse1> handleGenericException(Exception ex) {
	        DataResponse dataResponse = new DataResponse(
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            "An unexpected error occurred",
	            null
	        );
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse1(dataResponse));
	    }
	    
	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ApiResponse1> handleIllegalArgument(IllegalArgumentException ex) {
	        DataResponse dataResponse = new DataResponse(
	            HttpStatus.BAD_REQUEST.value(),  // or use CONFLICT if it's a duplicate
	            ex.getMessage(),
	            null
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse1(dataResponse));
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();
	        ex.getBindingResult().getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage())
	        );
	        return ResponseEntity.badRequest().body(errors);
	    }
	    @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
	        errorResponse.put("message", ex.getMessage());
//	        errorResponse.put("timestamp", LocalDateTime.now());
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    }

	    
	}