package Hutechlibrary.Anu.Library.dto;

import java.time.LocalDateTime;
import java.util.List;

import Hutechlibrary.Anu.Library.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDetails  {

    private int status;
    private String message;
    private Object users;
}
