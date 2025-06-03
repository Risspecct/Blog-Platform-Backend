package users.rishik.BlogPlatform.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Services.CommentService;

@RestController
public class CommentController {
    private final CommentService commentService;

    CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<?> addComment(@PathVariable long userId, @PathVariable long postId, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        try {
            return new ResponseEntity<>(this.commentService.addComment(userId, postId, message), HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<?> getAllComments(@PathVariable long postId){
        try {
            return ResponseEntity.ok(this.commentService.getCommentsByPostId(postId));
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("users/{userId}/comments")
    public ResponseEntity<?> getUserComments(@PathVariable long userId){
        try{
            return ResponseEntity.ok(this.commentService.getCommentsByUserId(userId));
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("users/{userId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable long userId, @PathVariable long commentId, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        try {
            return new ResponseEntity<>(this.commentService.updateComment(userId, commentId, message), HttpStatus.ACCEPTED);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("users/{userId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long userId, @PathVariable long commentId){
        try{
            this.commentService.deleteComment(userId, commentId);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
