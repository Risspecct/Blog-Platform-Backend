package users.rishik.BlogPlatform.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.UpdateUserDto;
import users.rishik.BlogPlatform.Dtos.UserDto;
import users.rishik.BlogPlatform.Projections.UserView;
import users.rishik.BlogPlatform.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto){
        return new ResponseEntity<>(this.userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id){
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(){
        List<UserView> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable long id,@RequestBody @Valid UpdateUserDto dto){
        return ResponseEntity.ok(this.userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        this.userService.deleteUser(id);
        return new ResponseEntity<>("User with id: " + id + " deleted", HttpStatus.ACCEPTED);
    }
}
