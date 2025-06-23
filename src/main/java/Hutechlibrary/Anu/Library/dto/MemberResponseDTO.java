package Hutechlibrary.Anu.Library.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MemberResponseDTO {
	
	  private Long id;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String phone;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;


}
