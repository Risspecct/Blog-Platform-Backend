package users.rishik.BlogPlatform.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Exceptions.UnauthorizedException;
import users.rishik.BlogPlatform.Mappers.PostMapper;
import users.rishik.BlogPlatform.Projections.PostView;
import users.rishik.BlogPlatform.Repositories.PostRepository;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;
    private PostDto postDto;
    private PostView postView;

    private final long userId = 1L;
    private final long postId = 10L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setUsername("author");

        postDto = new PostDto();
        postDto.setHeader("Test Header");
        postDto.setBody("Test Body");

        post = new Post();
        post.setId(postId);
        post.setHeader("Test Header");
        post.setBody("Test Body");
        post.setUser(user);

        // Mock PostView
        postView = new PostView() {
            @Override
            public long getId() { return postId; }
            @Override
            public String getHeader() { return "Test Header"; }
            @Override
            public String getBody() { return "Test Body"; }
            @Override
            public UserSummary getUser() {
                return () -> "author";
            }
        };
    }

    @Test
    void addPost_Success() {
        // Arrange
        // 1. Mock the mapper logic
        when(postMapper.fromDtoToPost(any(PostDto.class), eq(userRepository))).thenReturn(post);
        // 2. Mock the save call
        when(postRepository.save(any(Post.class))).thenReturn(post);
        // 3. Mock the final 'find' for the return value
        when(postRepository.findPostById(postId)).thenReturn(Optional.of(postView));

        // Act
        PostView result = postService.addPost(userId, postDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test Header", result.getHeader());

        // Verify the DTO was updated with the userId
        verify(postMapper).fromDtoToPost(argThat(dto -> dto.getUserId().equals(userId)), eq(userRepository));
        verify(postRepository).save(post);
        verify(postRepository).findPostById(postId);
    }

    @Test
    void getPost_Success() {
        // Arrange
        when(postRepository.findPostById(postId)).thenReturn(Optional.of(postView));

        // Act
        PostView result = postService.getPost(postId);

        // Assert
        assertNotNull(result);
        assertEquals(postView, result);
    }

    @Test
    void getPost_NotFound() {
        // Arrange
        when(postRepository.findPostById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> postService.getPost(postId));
    }

    @Test
    void updatePost_Success() {
        // Arrange
        UpdatePostDto updateDto = new UpdatePostDto();
        updateDto.setHeader("Updated Header");

        // 1. Mock the 'validate' method's first step
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // 2. Mock the mapper update
        // We use 'thenAnswer' to simulate the @MappingTarget update
        when(postMapper.UpdateFromDto(any(UpdatePostDto.class), eq(post))).thenAnswer(invocation -> {
            UpdatePostDto dto = invocation.getArgument(0);
            Post postToUpdate = invocation.getArgument(1);
            if (dto.getHeader() != null) {
                postToUpdate.setHeader(dto.getHeader());
            }
            if (dto.getBody() != null) {
                postToUpdate.setBody(dto.getBody());
            }
            return postToUpdate; // <-- Return the updated post, not null
        });

        // 3. Mock the save call
        when(postRepository.save(any(Post.class))).thenReturn(post);
        // 4. Mock the final 'find'
        when(postRepository.findPostById(postId)).thenReturn(Optional.of(postView));

        // Act
        PostView result = postService.updatePost(userId, postId, updateDto);

        // Assert
        assertNotNull(result);
        verify(postRepository).findById(postId); // Validate called
        verify(postMapper).UpdateFromDto(updateDto, post); // Mapper called
        verify(postRepository).save(post); // Save called
    }

    @Test
    void updatePost_Fails_Unauthorized() {
        // Arrange
        long otherUserId = 2L; // A different user
        UpdatePostDto updateDto = new UpdatePostDto();
        updateDto.setHeader("Updated Header");

        // 1. Mock the 'validate' method's find
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        // Note: post.getUser().getId() is 1L, but we are passing in otherUserId (2L)

        // Act & Assert
        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> postService.updatePost(otherUserId, postId, updateDto)
        );

        assertEquals("You are not the author of this post", ex.getMessage());
        verify(postRepository, never()).save(any()); // Save should never be called
    }

    @Test
    void deletePost_Success() {
        // Arrange
        // 1. Mock the 'validate' step
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // 2. Mock the delete call (it's void)
        doNothing().when(postRepository).deleteById(postId);

        // Act
        postService.deletePost(userId, postId);

        // Assert
        verify(postRepository).findById(postId); // Validation happened
        verify(postRepository).deleteById(postId); // Deletion happened
    }

    @Test
    void deletePost_Fails_Unauthorized() {
        // Arrange
        long otherUserId = 2L;
        // 1. Mock the 'validate' step
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act & Assert
        assertThrows(
                UnauthorizedException.class,
                () -> postService.deletePost(otherUserId, postId)
        );

        verify(postRepository, never()).deleteById(anyLong()); // Delete should never be called
    }
}