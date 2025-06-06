package users.rishik.BlogPlatform.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import users.rishik.BlogPlatform.Enums.Roles;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String pwd;

    private String bio;

    private Set<Roles> roles;
}
