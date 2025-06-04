package users.rishik.BlogPlatform.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Services.PostService;


@RestController
@RequestMapping("/users/{userId}/posts")
public class PostController {
    private final PostService postService;

    PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/")
    public ResponseEntity<?> addPost(@RequestBody @Valid PostDto postDto){
        return new ResponseEntity<>(this.postService.addPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id){
        return ResponseEntity.ok(this.postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPosts(@PathVariable long userId){
        return ResponseEntity.ok(this.postService.getByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable long userId, @PathVariable long id, @RequestBody @Valid UpdatePostDto postDto){
        return new ResponseEntity<>(this.postService.updatePost(userId, id, postDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long userId, @PathVariable long id) {
        this.postService.deletePost(userId, id);
        return ResponseEntity.ok("Post deleted Successfully");
    }
}
