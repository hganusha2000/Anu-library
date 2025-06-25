package Hutechlibrary.Anu.Library.controller;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.ApiResponse1;
import Hutechlibrary.Anu.Library.dto.ApiResponseUser;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.PasswordResetForm;
import Hutechlibrary.Anu.Library.dto.PasswordResetRequest;
import Hutechlibrary.Anu.Library.dto.UserDetails;
import Hutechlibrary.Anu.Library.dto.UserResponseDTO;
import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponseUser> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));

        List<UserResponseDTO> dtoList = userPage.getContent().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.isActivated(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());

        ApiResponseUser response = new ApiResponseUser(
                HttpStatus.OK.value(),
                "fetched successfully",
                dtoList,
                userPage.getTotalPages(),
                userPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }
    
 // Step 1: Request password reset
    @PostMapping("/reset-password/request")
    public ResponseEntity<ApiResponse1> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        try {
            userService.initiatePasswordReset(request.getEmail());
            DataResponse response = new DataResponse(HttpStatus.OK.value(), "Password reset link sent to email", null);
            return ResponseEntity.ok(new ApiResponse1(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse1(new DataResponse(400, e.getMessage(), null)));
        }
    }

    // Step 2: Reset password using token
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<ApiResponse1> confirmPasswordReset(@RequestBody PasswordResetForm form) {
        try {
            userService.resetPassword(form.getToken(), form.getNewPassword());
            DataResponse response = new DataResponse(HttpStatus.OK.value(), "Password reset successfully", null);
            return ResponseEntity.ok(new ApiResponse1(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse1(new DataResponse(400, e.getMessage(), null)));
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDetails> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);

        UserDetails response = new UserDetails(
                HttpStatus.OK.value(),
                "User deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }
}