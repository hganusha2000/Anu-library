package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookDetails {
	
	
	List<DataResponse> data;
	
	  private int totalPages;
	  private long totalElements;
	
}