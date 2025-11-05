package users.rishik.BlogPlatform.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Enums.Roles;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Mappers.UserMapper;
import users.rishik.BlogPlatform.Projections.UserView;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    // We don't mock the encoder, as the service creates it directly[cite: 349].
    // We'll verify the logic that uses it.

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;
    private UserView userView;

    @BeforeEach
    void setUp() {
        // Shared test data
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPwd("password123");
        userDto.setBio("A test bio");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPwd("encodedPassword123"); // Simulating encoded password
        user.setRoles(Set.of(Roles.VIEWER));

        // Mock UserView
        userView = new UserView() {
            @Override
            public long getId() { return 1L; }
            @Override
            public String getUsername() { return "testuser"; }
            @Override
            public String getBio() { return "A test bio"; }
            @Override
            public Roles getRoles() { return Roles.VIEWER; }
        };
    }

    @Test
    void addUser_Success() {
        // Arrange
        // 1. DTO with default 'VIEWER' role [cite: 352]
        userDto.setRoles(Set.of(Roles.VIEWER));

        // 2. Mock email check (email does not exist) [cite: 353]
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        // 3. Mock mapper (DTO -> Entity) [cite: 354]
        // The service encodes the password *before* mapping
        when(userMapper.UserDtoToUser(any(UserDto.class))).thenReturn(user);

        // 4. Mock repository save [cite: 354]
        when(userRepository.save(any(User.class))).thenReturn(user);

        // 5. Mock final 'find' operation to return the view [cite: 354]
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(userView));

        // Act
        UserView result = userService.addUser(userDto);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());

        // Verify mocks were called correctly
        verify(userRepository).existsByEmail("test@example.com");
        verify(userMapper).UserDtoToUser(argThat(dto ->
                dto.getEmail().equals("test@example.com") &&
                        !dto.getPwd().equals("password123") // Check that password was encoded
        ));
        verify(userRepository).save(user);
        verify(userRepository).findUserById(1L);
    }

    @Test
    void addUser_EmailAlreadyExists() {
        // Arrange
        // Mock email check (email *does* exist) [cite: 353]
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDto)
        );

        assertEquals("Email already exists", exception.getMessage());

        // Verify that no other methods were called
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).UserDtoToUser(any());
    }

    @Test
    void addUser_CannotAssignAdminRole() {
        // Arrange
        userDto.setRoles(Set.of(Roles.ADMIN));// Attempt to set ADMIN role

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.addUser(userDto)
        );

        assertEquals("Cannot add user with this role. Ask admin for permission", exception.getMessage());
        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    void getUser_Success() {
        // Arrange
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(userView));

        // Act
        UserView result = userService.getUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(userView, result);
        verify(userRepository).findUserById(1L);
    }

    @Test
    void getUser_NotFound() {
        // Arrange
        when(userRepository.findUserById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getUser(99L)
        );

        assertEquals("User not found with id: 99", exception.getMessage());
    }
}