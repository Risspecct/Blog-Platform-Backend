package users.rishik.BlogPlatform.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.rishik.BlogPlatform.Entities.Like;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Projections.LikeView;
import users.rishik.BlogPlatform.Repositories.LikeRepository;
import users.rishik.BlogPlatform.Repositories.PostRepository;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private LikeService likeService;

    private User user;
    private Post post;
    private final long userId = 1L;
    private final long postId = 10L;
    private final long likeId = 100L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);

        post = new Post();
        post.setId(postId);
    }

    @Test
    void addLike_Success() {
        // Arrange
        // 1. Check for existing like (does not exist)
        when(likeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);
        // 2. Find user and post
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // 3. Mock the save
        when(likeRepository.save(any(Like.class))).thenAnswer(invocation -> {
            Like like = invocation.getArgument(0);
            like.setId(likeId); // Simulate DB assigning ID
            return like;
        });

        // 4. Mock the final 'find' for the return
        LikeView likeView = mock(LikeView.class);
        when(likeRepository.findLikeById(likeId)).thenReturn(Optional.of(likeView));

        // Act
        LikeView result = likeService.addLike(userId, postId);

        // Assert
        assertNotNull(result);
        verify(likeRepository).existsByUserIdAndPostId(userId, postId);
        verify(userRepository).findById(userId);
        verify(postRepository).findById(postId);
        verify(likeRepository).save(any(Like.class));
        verify(likeRepository).findLikeById(likeId);
    }

    @Test
    void addLike_Fails_AlreadyLiked() {
        // Arrange
        // 1. Check for existing like (DOES exist)
        when(likeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);

        // Act & Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> likeService.addLike(userId, postId)
        );

        assertEquals("Already liked the Post", ex.getMessage());
        verify(userRepository, never()).findById(anyLong());
        verify(postRepository, never()).findById(anyLong());
        verify(likeRepository, never()).save(any());
    }

    @Test
    void addLike_Fails_UserNotFound() {
        // Arrange
        when(likeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> likeService.addLike(userId, postId));
    }

    @Test
    void getLikesByPost_Success() {
        // Arrange
        when(likeRepository.countByPostId(postId)).thenReturn(5L);

        // Act
        long likeCount = likeService.getLikesByPost(postId);

        // Assert
        assertEquals(5L, likeCount);
        verify(likeRepository).countByPostId(postId);
    }

    @Test
    void removeLike_Success() {
        // Arrange
        // Void method, so we just check it's called
        doNothing().when(likeRepository).deleteByUserIdAndPostId(userId, postId);

        // Act
        likeService.removeLike(userId, postId);

        // Assert
        verify(likeRepository).deleteByUserIdAndPostId(userId, postId);
    }
}