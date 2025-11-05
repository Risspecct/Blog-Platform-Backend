package users.rishik.BlogPlatform.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Security.SecurityConfig;
import users.rishik.BlogPlatform.Services.CommentService;
import users.rishik.BlogPlatform.Services.JwtService;
import users.rishik.BlogPlatform.Services.MyUserDetailsService;
import users.rishik.BlogPlatform.Services.PostService;
import users.rishik.BlogPlatform.Services.UserService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(PostController.class)
class PostControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    // We must mock all dependencies for the security config, even if unused
    @MockitoBean private UserService userService;
    @MockitoBean private CommentService commentService;
    @MockitoBean private MyUserDetailsService myUserDetailsService;
    @MockitoBean private JwtService jwtService;

    @Test
    @WithMockUser(roles = "AUTHOR") // Mocks an authenticated user with this role
    void addPost_Fails_Validation() throws Exception {
        // Arrange
        PostDto invalidDto = new PostDto();
        invalidDto.setHeader(""); // @NotBlank constraint
        invalidDto.setBody(null); // @NotBlank constraint

        // Act & Assert
        // Perform a POST request and check for the 400 Bad Request
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    @WithMockUser(roles = "VIEWER") // This user is authenticated
    void getPost_Fails_NotFound() throws Exception {
        // Arrange
        // Mock the service to throw the exception
        when(postService.getPost(anyLong()))
                .thenThrow(new NotFoundException("Post not found with id: 99"));

        // Act & Assert
        // The @GlobalExceptionHandler should catch this and turn it into a 404
        mockMvc.perform(get("/posts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Post not found with id: 99"));
    }

    @Test
    @WithMockUser(roles = "VIEWER") // This role is not enough
    void addPost_Fails_Authorization() throws Exception {
        // Arrange
        // This endpoint requires 'AUTHOR'
        PostDto postDto = new PostDto();
        postDto.setHeader("A valid header");
        postDto.setBody("A valid body");

        // Act & Assert
        // We expect a 403 Forbidden
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }
}