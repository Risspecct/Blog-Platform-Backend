package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Enums.Roles;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.util.Set;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepository;

    AdminController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Operation( summary = "Change user's role", description = "Admins can update roles of any user", tags = {"ADMIN"})
    @PutMapping("users/roles/{userId}")
    public ResponseEntity<?> setUserRole(@PathVariable long userId, @RequestBody Set<Roles> roles){
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        user.setRoles(roles);
        return new ResponseEntity<>(this.userRepository.save(user), HttpStatus.OK);
    }

    @Operation( summary = "Ban a user", description = "Admins can use this endpoint to ban any user", tags = {"ADMIN"})
    @PutMapping("users/ban/{userId}")
    public ResponseEntity<?> banUser(@PathVariable long userId, @RequestBody boolean banStatus){
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        user.setBanned(banStatus);
        this.userRepository.save(user);
        return ResponseEntity.ok("The user: " + userId + " ban status is set to: " + banStatus);
    }

    @Operation( summary = "Delete a user's account", description = "Admins can use this endpoint to delete a user's account",
            tags = {"ADMIN"})
    @DeleteMapping("users/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        if (this.userRepository.existsById(userId)) {
            this.userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } else throw new NotFoundException("No user found with user id: " + userId);
    }
}
