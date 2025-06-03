package users.rishik.BlogPlatform.Dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import users.rishik.BlogPlatform.Annotations.NullOrNotBlank;

@Data
@NoArgsConstructor
public class UpdatePostDto {

    @NullOrNotBlank
    private String header;

    @NullOrNotBlank
    private String body;
}
