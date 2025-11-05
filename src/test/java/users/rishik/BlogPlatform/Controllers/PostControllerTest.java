package users.rishik.BlogPlatform.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Enums.Roles;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Exceptions.UnauthorizedException;
import users.rishik.BlogPlatform.Projections.PostView;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.PostService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private UserPrincipal mockUserPrincipal;
    private PostView mockPostView;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        // Create a mock User object
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("author@example.com");
        mockUser.setRoles(java.util.Set.of(Roles.AUTHOR));

        // Create the UserPrincipal that Spring Security would normally provide
        mockUserPrincipal = new UserPrincipal(mockUser);

        // Mock DTO
        postDto = new PostDto();
        postDto.setHeader("New Post");
        postDto.setBody("Post body");

        // Mock Projection
        mockPostView = new PostView() {
            public long getId() { return 10L; }
            public String getHeader() { return "New Post"; }
            public String getBody() { return "Post body"; }
            public UserSummary getUser() { return () -> "author@example.com"; }
        };
    }

    @Test
    void addPost_Success() {
        // Arrange
        // Mock the service call
        when(postService.addPost(eq(1L), any(PostDto.class))).thenReturn(mockPostView);

        // Act
        // Call the method directly, passing our mock principal
        ResponseEntity<?> response = postController.addPost(mockUserPrincipal, postDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockPostView, response.getBody());
    }

    @Test
    void getPost_Success() {
        // Arrange
        when(postService.getPost(10L)).thenReturn(mockPostView);

        // Act
        ResponseEntity<?> response = postController.getPost(10L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPostView, response.getBody());
    }

    @Test
    void getPost_NotFound() {
        // Arrange
        when(postService.getPost(99L)).thenThrow(new NotFoundException("Post unavailable"));

        // Act & Assert
        // We assert that the exception is thrown *by the controller*
        // In a real app, @GlobalExceptionHandler would catch this.
        assertThrows(NotFoundException.class, () -> {
            postController.getPost(99L);
        });
    }

    @Test
    void deletePost_Fails_Unauthorized() {
        // Arrange
        long postId = 10L;
        // Mock the service to throw an exception
        doThrow(new UnauthorizedException("You are not the author"))
                .when(postService).deletePost(eq(1L), eq(postId));

        // Act & Assert
        // We test that the controller propagates this exception
        assertThrows(UnauthorizedException.class, () -> {
            postController.deletePost(mockUserPrincipal, postId);
        });
    }
}