package Hutechlibrary.Anu.Library.controller;

import Hutechlibrary.Anu.Library.entity.User;
import Hutechlibrary.Anu.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 🔓 Accessible by any authenticated user
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 🔐 Only ADMINs can delete a user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
