package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponseMember {
	private int status;
	private String message;
    private List<MemberResponseDTO> members;
	private int totalPages;
	private long totalElements;

}
