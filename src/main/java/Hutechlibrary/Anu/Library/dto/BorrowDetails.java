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

    private int status;
    private String message;
    private Object borrows;
		
	}
	
