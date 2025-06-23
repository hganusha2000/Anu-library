package Hutechlibrary.Anu.Library.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookResponseDTO {
	
    private Long id;
    private String title;
    private String author;  // Just the name
    private String isbn;
    private Boolean available;

}
