package Hutechlibrary.Anu.Library.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private Long userId;
    private String username;
    private String userRole;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean returned;
	private Long libraryId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
