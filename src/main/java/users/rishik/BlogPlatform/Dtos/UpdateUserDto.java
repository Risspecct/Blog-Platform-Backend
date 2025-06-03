package users.rishik.BlogPlatform.Dtos;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import users.rishik.BlogPlatform.Annotations.NullOrNotBlank;

@Data
@NoArgsConstructor
public class UpdateUserDto {
    @NullOrNotBlank
    private String username;

    @Email
    @NullOrNotBlank
    private String email;

    @NullOrNotBlank
    private String pwd;

    private String bio;
}
