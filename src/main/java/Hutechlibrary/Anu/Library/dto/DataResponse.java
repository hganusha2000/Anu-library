package Hutechlibrary.Anu.Library.dto;

import Hutechlibrary.Anu.Library.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DataResponse {
	
    private int status;
    private String message;
    private Object data;
}

