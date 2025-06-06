package users.rishik.BlogPlatform.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.PostService;


@RestController
public class PostController {
    private final PostService postService;

    PostController(PostService postService){
        this.postService = postService;
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/posts")
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostDto postDto){
        return new ResponseEntity<>(this.postService.addPost(userPrincipal.getUserId(), postDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("posts/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id){
        return ResponseEntity.ok(this.postService.getPost(id));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/users/me/posts")
    public ResponseEntity<?> getAllPosts(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.postService.getByUserId(userPrincipal.getUserId()));
    }

    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getAllPostsByUser(@PathVariable long userId){
        return ResponseEntity.ok(this.postService.getByUserId(userId));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("posts/{id}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody @Valid UpdatePostDto postDto){
        return new ResponseEntity<>(this.postService.updatePost(userPrincipal.getUserId(), id, postDto), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        this.postService.deletePost(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Post deleted Successfully");
    }
}
