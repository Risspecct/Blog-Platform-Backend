package users.rishik.BlogPlatform.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    @NotBlank
    private String header;

    @NotBlank
    private String body;


    private Long userId;

    public Long getUserId(){
        return userId;
    }

    public void setUserId(long userId) { this.userId = userId; }
}
