package Hutechlibrary.Anu.Library.controller;

import Hutechlibrary.Anu.Library.dto.ApiResponse;
import Hutechlibrary.Anu.Library.dto.DataResponse;
import Hutechlibrary.Anu.Library.dto.PageUserDetails;
import Hutechlibrary.Anu.Library.dto.UserDetails;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.getAllUsers(pageable);

        // Create a custom response DTO with users and pagination metadata
        PageUserDetails userPageResponse = new PageUserDetails(
        	    userPage.getContent().stream()
        	        .map(user -> new UserDetails(
        	            user.getId(),
        	            user.getUsername(),
        	            user.getEmail(),
        	            user.isActivated(),
        	            user.getCreatedAt()
        	        )).toList(),  // ⬅ ensure you are converting to List<UserDetails>
        	    userPage.getTotalPages(),
        	    userPage.getTotalElements()
        	);
        DataResponse response = new DataResponse(
            HttpStatus.OK.value(),
            "Users fetched successfully",
            userPageResponse
        );

        return ResponseEntity.ok(new ApiResponse(response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        DataResponse response = new DataResponse(HttpStatus.OK.value(), "User deleted successfully", null);
        return ResponseEntity.ok(new ApiResponse(response));
    }
}