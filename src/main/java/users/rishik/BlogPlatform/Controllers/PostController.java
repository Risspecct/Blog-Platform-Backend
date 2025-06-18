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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "403", description = "Not logged in")
    })
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/posts")
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostDto postDto){
        return new ResponseEntity<>(this.postService.addPost(userPrincipal.getUserId(), postDto), HttpStatus.CREATED);
    }

    @Operation( summary = "View a post", description = "Users can view a specified post using this endpoint",
            tags = {"USER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "403", description = "Not logged in")
    })
    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("posts/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id){
        return ResponseEntity.ok(this.postService.getPost(id));
    }

    @Operation( summary = "View my posts", description = "Authors can view all their posts", tags = {"USER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved"),
            @ApiResponse(responseCode = "404", description = "Posts not found"),
            @ApiResponse(responseCode = "403", description = "Not logged in")
    })
    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/users/me/posts")
    public ResponseEntity<?> getAllPosts(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.postService.getByUserId(userPrincipal.getUserId()));
    }

    @Operation( summary = "View all posts", description = "Users can view all the posts from a user using this endpoint",
            tags = {"USER"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved"),
            @ApiResponse(responseCode = "404", description = "Posts not found"),
            @ApiResponse(responseCode = "403", description = "Not logged in")
    })
    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getAllPostsByUser(@PathVariable long userId){
        return ResponseEntity.ok(this.postService.getByUserId(userId));
    }

    @Operation( summary = "Update your post", description = "Authors can update their posts using this endpoint",
            tags = {"AUTHOR"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Post updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "403", description = "Not logged in")
    })
    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("posts/{id}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @PathVariable long id, @RequestBody @Valid UpdatePostDto postDto){
        return new ResponseEntity<>(this.postService.updatePost(userPrincipal.getUserId(), id, postDto), HttpStatus.ACCEPTED);
    }

    @Operation( summary = "Delete your posts", description = "Authors can delete their own posts using this endpoint",
            tags = {"AUTHOR"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "403", description = "Not logged in")
    })
    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        this.postService.deletePost(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Post deleted Successfully");
    }
}
