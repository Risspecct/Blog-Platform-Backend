package users.rishik.BlogPlatform.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.UpdateUserDto;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Entities.User;
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

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto){
        return new ResponseEntity<>(this.userService.addUser(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid User user){
        return new ResponseEntity<>(this.userService.verify(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.userService.getUser(userPrincipal.getUserId()));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(){
        List<UserView> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getUserComments(@PathVariable long userId){
        return ResponseEntity.ok(this.commentService.getCommentsByUserId(userId));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateUserDto dto){
        return ResponseEntity.ok(this.userService.updateUser(userPrincipal.getUserId(), dto));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        this.userService.deleteUser(userPrincipal.getUserId());
        return new ResponseEntity<>("User with id: " + userPrincipal.getUserId() + " deleted", HttpStatus.ACCEPTED);
    }
}
