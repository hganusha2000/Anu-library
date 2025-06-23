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

public class BorrowResponseDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long memberId;
//    private String memberName;
    private LocalDateTime borrowDate;
    private LocalDate returnDate;
    private boolean returned;
	
	
	
}
