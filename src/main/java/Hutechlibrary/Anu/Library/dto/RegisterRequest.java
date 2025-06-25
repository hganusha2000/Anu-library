package Hutechlibrary.Anu.Library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class RegisterRequest {


	    @NotBlank(message = "Username is required")
	    private String username;

	    @NotBlank(message = "Email is required")
	    @Email(message = "Invalid email format")
	    private String email;

	    @NotBlank(message = "Password is required")
	    @Pattern(
	        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
	        message = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character"
	    )
	    private String password;

	    @NotBlank(message = "Role is required")
	    private String role;
	    
	    @NotBlank(message = "First name is required")
	    private String firstName;

	    @NotBlank(message = "Last name is required")
	    private String lastName;

	    @NotBlank(message = "Phone number is required")
	    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
	    private String phone;
	}
