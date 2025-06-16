package users.rishik.BlogPlatform.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginUserDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String pwd;
}
