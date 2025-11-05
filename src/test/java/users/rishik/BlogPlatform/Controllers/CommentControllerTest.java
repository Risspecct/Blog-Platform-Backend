package users.rishik.BlogPlatform.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Enums.Roles;
import users.rishik.BlogPlatform.Projections.CommentView;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.CommentService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private UserPrincipal mockUserPrincipal;
    private final long userId = 1L;
    private final long postId = 10L;
    private final long commentId = 100L;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(userId);
        user.setRoles(Set.of(Roles.VIEWER));
        mockUserPrincipal = new UserPrincipal(user);
    }

    @Test
    void addComment_Success() {
        // Arrange
        String message = "This is a comment";
        String cleanMessage = "This is a comment";

        CommentView mockCommentView = mock(CommentView.class);

        // Test the service call with the *cleaned* message
        when(commentService.addComment(userId, postId, cleanMessage))
                .thenReturn(mockCommentView);

        // Act
        // The controller receives a raw string, often with quotes
        ResponseEntity<?> response = commentController.addComment(
                mockUserPrincipal, postId, "\"" + message + "\"");

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockCommentView, response.getBody());
    }

    @Test
    void deleteComment_Success() {
        // Arrange
        // No need to mock 'deleteComment' as it's void

        // Act
        ResponseEntity<?> response = commentController.deleteComment(
                mockUserPrincipal, commentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully", response.getBody());
    }
}