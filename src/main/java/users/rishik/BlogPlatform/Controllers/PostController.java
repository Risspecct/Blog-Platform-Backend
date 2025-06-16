package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation( summary = "Add a post", description = "Authors can add posts using this endpoint", tags = {"AUTHOR"})
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/posts")
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostDto postDto){
        return new ResponseEntity<>(this.postService.addPost(userPrincipal.getUserId(), postDto), HttpStatus.CREATED);
    }

    @Operation( summary = "View a post", description = "Users can view a specified post using this endpoint",
            tags = {"USER"})
    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("posts/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id){
        return ResponseEntity.ok(this.postService.getPost(id));
    }

    @Operation( summary = "View my posts", description = "Authors can view all their posts", tags = {"USER"})
    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/users/me/posts")
    public ResponseEntity<?> getAllPosts(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.postService.getByUserId(userPrincipal.getUserId()));
    }

    @Operation( summary = "View all posts", description = "Users can view all the posts from a user using this endpoint",
            tags = {"USER"})
    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getAllPostsByUser(@PathVariable long userId){
        return ResponseEntity.ok(this.postService.getByUserId(userId));
    }

    @Operation( summary = "Update your post", description = "Authors can update their posts using this endpoint",
            tags = {"AUTHOR"})
    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("posts/{id}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @PathVariable long id, @RequestBody @Valid UpdatePostDto postDto){
        return new ResponseEntity<>(this.postService.updatePost(userPrincipal.getUserId(), id, postDto), HttpStatus.ACCEPTED);
    }

    @Operation( summary = "Delete your posts", description = "Authors can delete their own posts using this endpoint",
            tags = {"AUTHOR"})
    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        this.postService.deletePost(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Post deleted Successfully");
    }
}
