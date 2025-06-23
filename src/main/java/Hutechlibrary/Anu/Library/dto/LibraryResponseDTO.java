package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LibraryResponseDTO {
	
    private Long id;
    private String name;
    private String address;
    private List<Long> bookIds;
    private List<Long> userIds;
    private List<Long> borrowIds;

}
