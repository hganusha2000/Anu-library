package Hutechlibrary.Anu.Library.dto;

import lombok.Data;

@Data

public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String role; // e.g., ADMIN, LIBRARIAN, USER
}
