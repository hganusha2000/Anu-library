package Hutechlibrary.Anu.Library.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherResponseDTO {
	
	 private Long id;
	    private String name;
	    private String address;
	    private LocalDateTime recordedAt;

}
