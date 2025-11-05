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
import users.rishik.BlogPlatform.Projections.LikeView;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.LikeService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    private UserPrincipal mockUserPrincipal;
    private final long userId = 1L;
    private final long postId = 10L;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(userId);
        user.setRoles(Set.of(Roles.VIEWER));
        mockUserPrincipal = new UserPrincipal(user);
    }

    @Test
    void like_Success() {
        // Arrange
        LikeView mockLikeView = mock(LikeView.class);
        when(likeService.addLike(userId, postId)).thenReturn(mockLikeView);

        // Act
        ResponseEntity<?> response = likeController.like(mockUserPrincipal, postId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockLikeView, response.getBody());
    }

    @Test
    void countLikes_Success() {
        // Arrange
        when(likeService.getLikesByPost(postId)).thenReturn(5L);

        // Act
        ResponseEntity<?> response = likeController.countLikes(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    @Test
    void removeLike_Success() {
        // Arrange
        // No mocking needed for void service method

        // Act
        ResponseEntity<?> response = likeController.removeLike(mockUserPrincipal, postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Removed Like", response.getBody());
    }
}