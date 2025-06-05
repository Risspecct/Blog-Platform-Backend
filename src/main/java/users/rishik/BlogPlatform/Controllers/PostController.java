package users.rishik.BlogPlatform.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.PostService;


@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/")
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostDto postDto){
        return new ResponseEntity<>(this.postService.addPost(userPrincipal.getUserId(), postDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id){
        return ResponseEntity.ok(this.postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPosts(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.postService.getByUserId(userPrincipal.getUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody @Valid UpdatePostDto postDto){
        return new ResponseEntity<>(this.postService.updatePost(userPrincipal.getUserId(), id, postDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        this.postService.deletePost(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Post deleted Successfully");
    }
}
