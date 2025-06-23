package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import Hutechlibrary.Anu.Library.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponse {

	private List<AuthorDetails> authors;
	private int totalPages;
	private long totalElements;
    
}
