package csendes.david.ser.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "Can't be blank")
    @Size(min = 10,
            max = 50,
            message = "10<x<50")
    private String name;
    private String username;

    @NotBlank(message = "Can't be blank")
    @Size(min = 4,
            max = 40,
            message = "4<x<40")
    private String password;

    @Email
    private String email;
}
