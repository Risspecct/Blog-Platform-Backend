package users.rishik.BlogPlatform.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.CommentService;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService
                .addComment(userPrincipal.getUserId(), postId, message), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComment(@PathVariable long id){
        return ResponseEntity.ok(this.commentService.getComment(id));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllComments(@PathVariable long postId){
        return ResponseEntity.ok(this.commentService.getCommentsByPostId(postId));
    }

    @PutMapping("/{Id}")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long Id, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService.updateComment(userPrincipal.getUserId(), Id, message), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id){
        this.commentService.deleteComment(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
