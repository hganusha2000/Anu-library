package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PageBorrowDetails {
	

    private List<BorrowDetails> borrows;  // ✅ Correct field name
    private int totalPages;
    private long totalElements;
	
	

}
