package users.rishik.BlogPlatform.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Enums.Roles;
import users.rishik.BlogPlatform.Projections.UserView;
import users.rishik.BlogPlatform.Services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Use the standard Mockito extension, NOT a Spring one
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    // Mock the dependencies the controller needs
    @Mock
    private UserService userService;

    // Manually create an instance of the controller
    // and inject the mocks into it.
    @InjectMocks
    private UserController userController;

    private UserView userView;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("newUser");
        userDto.setEmail("new@example.com");
        userDto.setPwd("ValidPassword123");

        // Mock UserView
        userView = new UserView() {
            @Override
            public long getId() { return 1L; }
            @Override
            public String getUsername() { return "newUser"; }
            @Override
            public String getBio() { return null; }
            @Override
            public Roles getRoles() { return Roles.VIEWER; }
        };
    }

    @Test
    void registerUser_Success() {
        // Arrange
        // Mock the service layer
        when(userService.addUser(any(UserDto.class))).thenReturn(userView);

        // Act
        // We call the method directly. No MockMvc.
        ResponseEntity<?> response = userController.addUser(userDto);

        // Assert
        // We check the ResponseEntity itself.
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userView, response.getBody());
    }

    @Test
    void registerUser_EmailConflict() {
        // Arrange
        // Mock the service to throw the exception
        when(userService.addUser(any(UserDto.class)))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        // Act & Assert
        // We can't test the @GlobalExceptionHandler
        // We must test that the controller *throws* the exception,
        // (or in this case, test the service call that throws it)
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userController.addUser(userDto)
        );

        assertEquals("Email already exists", ex.getMessage());
    }
}