package users.rishik.BlogPlatform.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Dtos.PostDto;
import users.rishik.BlogPlatform.Dtos.UpdatePostDto;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Services.PostService;

import java.util.Map;

@RestController
@RequestMapping("/users/{userId}/posts")
public class PostController {
    private final PostService postService;

    PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto){
        try {
            return new ResponseEntity<>(this.postService.addPost(postDto), HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id){
        try {
            return ResponseEntity.ok(this.postService.getPost(id));
        } catch (NotFoundException e){
            return new ResponseEntity<>(Map.of("Error", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPosts(@PathVariable long userId){
        try {
            return ResponseEntity.ok(this.postService.getByUserId(userId));
        } catch (NotFoundException e){
            return new ResponseEntity<>(Map.of("Error", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable long userId, @PathVariable long id, @RequestBody UpdatePostDto postDto){
        try {
            return new ResponseEntity<>(this.postService.updatePost(userId, id, postDto), HttpStatus.ACCEPTED);
        } catch (NotFoundException e){
            return new ResponseEntity<>(Map.of("Error", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long userId, @PathVariable long id){
        try{
            this.postService.deletePost(userId, id);
            return ResponseEntity.ok("Post deleted Successfully");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
