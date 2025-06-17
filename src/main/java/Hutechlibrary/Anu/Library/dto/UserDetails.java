package Hutechlibrary.Anu.Library.dto;

import java.util.List;

import Hutechlibrary.Anu.Library.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDetails  {

    private List<User> data;     // Changed from List<DataResponse> to List<User>
    private int totalPages;
    private long totalElements;
}
