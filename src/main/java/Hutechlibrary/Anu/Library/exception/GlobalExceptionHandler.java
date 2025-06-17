package Hutechlibrary.Anu.Library.exception;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Failed");
    }

}
