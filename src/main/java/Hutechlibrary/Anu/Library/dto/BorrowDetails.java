package Hutechlibrary.Anu.Library.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class BorrowDetails {

    private Long id;
    private Long bookId;
    private String bookTitle; // optional: if you want to show book name
    private Long memberId;
    private String memberName; // optional: if you want to show member name
    private LocalDateTime borrowDate;
    private LocalDate returnDate;
    private boolean returned;
	public void setData(List<DataResponse> borrowDataList) {
		
	}
		
	}
	
