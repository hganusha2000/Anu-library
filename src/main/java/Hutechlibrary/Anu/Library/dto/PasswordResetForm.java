package Hutechlibrary.Anu.Library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PasswordResetForm {
	
    private String token;
    private String newPassword;

}
