package users.rishik.BlogPlatform.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return new ResponseEntity<>(this.commentService.addComment(userId, postId, message), HttpStatus.CREATED);
    }

    @GetMapping("posts/comments/{id}")
    public ResponseEntity<?> getComment(@PathVariable long id){
        return ResponseEntity.ok(this.commentService.getComment(id));
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<?> getAllComments(@PathVariable long postId){
        return ResponseEntity.ok(this.commentService.getCommentsByPostId(postId));
    }

    @GetMapping("users/{userId}/comments")
    public ResponseEntity<?> getUserComments(@PathVariable long userId){
        return ResponseEntity.ok(this.commentService.getCommentsByUserId(userId));
    }

    @PutMapping("users/{userId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable long userId, @PathVariable long commentId, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService.updateComment(userId, commentId, message), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("users/{userId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long userId, @PathVariable long commentId){
        this.commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
