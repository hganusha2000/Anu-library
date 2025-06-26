package Hutechlibrary.Anu.Library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.ApiResponse1;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.LoginRequest;
import Hutechlibrary.Anu.Library.dto.RegisterRequest;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.service.JwtService;
import Hutechlibrary.Anu.Library.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    


    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse1> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            DataResponse error = new DataResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse1(error));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        Map<String, Object> tokenData = jwtService.generateTokenWithExpiry(userDetails);

        // ✅ Insert token first, then expiresAt
        Map<String, Object> loginData = new LinkedHashMap<>();
        loginData.put("token", tokenData.get("token"));
        loginData.put("expiresAt", tokenData.get("expiresAt"));

        DataResponse success = new DataResponse(
            HttpStatus.OK.value(),
            "Login successful",
            loginData
        );

        return ResponseEntity.ok(new ApiResponse1(success));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse1> register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        try {
            User user = userService.registerUser(registerRequest);

            String role = registerRequest.getRole().toUpperCase();
            String message = switch (role) {
                case "ADMIN" -> "Admin registered successfully";
                case "LIBRARIAN" -> "Librarian registered successfully";
                case "USER" -> "User registered successfully and added as member";
                default -> "User registered";
            };

            DataResponse dataResponse = new DataResponse(
                HttpStatus.CREATED.value(),
                message,
                user
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse1(dataResponse));

        } catch (IllegalArgumentException e) {
            DataResponse error = new DataResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
            );
            return ResponseEntity.badRequest().body(new ApiResponse1(error));
        }
    }

    // ✅ ACCOUNT ACTIVATION
    @GetMapping("/activate")
    public ResponseEntity<ApiResponse1> activateAccount(@RequestParam String token) {
        try {
            userService.activateUser(token);
            DataResponse dataResponse = new DataResponse(
                HttpStatus.OK.value(),
                "Account activated successfully",
                null
            );
            return ResponseEntity.ok(new ApiResponse1(dataResponse));
        } catch (IllegalArgumentException e) {
            DataResponse error = new DataResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
            );
            return ResponseEntity.badRequest().body(new ApiResponse1(error));
        }
    }
}