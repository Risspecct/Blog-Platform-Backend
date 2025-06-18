package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.LoginUserDto;
import users.rishik.BlogPlatform.Dtos.UpdateUserDto;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Projections.UserView;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.CommentService;
import users.rishik.BlogPlatform.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CommentService commentService;

    UserController(UserService userService, CommentService commentService){
        this.userService = userService;
        this.commentService = commentService;
    }

    @Operation( summary = "Add user account", description = "This endpoint is used to add a user account to the platform",
                tags = {"AUTH"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing fields"),
            @ApiResponse(responseCode = "409", description = "Email already in use"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto){
        return new ResponseEntity<>(this.userService.addUser(userDto), HttpStatus.CREATED);
    }

    @Operation( summary = "User Login", description = "Users can login using their email and password",
                tags = {"AUTH"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid login data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDto user){
        return new ResponseEntity<>(this.userService.verify(user), HttpStatus.OK);
    }

    @Operation( summary = "View your profile", description = "This endpoint is used to show user their own details", tags = {"USER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.userService.getUser(userPrincipal.getUserId()));
    }

    @Operation( summary = "View all the users",
            description = "This endpoint can be use to display list of all users on this platform",
            tags = {"ADMIN"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users data retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        List<UserView> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation( summary = "View your comments", description = "User can get all the comments they've posted on this platform",
            tags = {"USER"})
    @PreAuthorize("hasRole('VIEWER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User comments retrieved"),
    })
    @GetMapping("/comments")
    public ResponseEntity<?> getUserComments(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.commentService.getCommentsByUserId(userPrincipal.getUserId()));
    }

    @Operation( summary = "Update your profile", description = "Users can update their profile with this endpoint.",
            tags = {"USER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid login data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('VIEWER')")
    @PutMapping("/")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateUserDto dto){
        return ResponseEntity.ok(this.userService.updateUser(userPrincipal.getUserId(), dto));
    }

    @Operation( summary = "Delete your account", description = "Users can delete their own account", tags = {"USER"})
    @PreAuthorize("hasRole('VIEWER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        this.userService.deleteUser(userPrincipal.getUserId());
        return new ResponseEntity<>("User with id: " + userPrincipal.getUserId() + " deleted", HttpStatus.ACCEPTED);
    }
}
