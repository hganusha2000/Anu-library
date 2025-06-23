package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponseBorrow {
	private int status;
	private String message;
	private List<BorrowResponseDTO> borrows;
	private int totalPages;
	private long totalElements;

}
