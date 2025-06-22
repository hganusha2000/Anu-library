package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor



public class PageBookDetails {
	
    private List<BookDetails> books;
    private int totalPages;
    private long totalElements;
	

}
