package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.CommentService;

@RestController
@RequestMapping("/posts/{postId}/comments")
@PreAuthorize("hasRole('VIEWER')")
public class CommentController {
    private final CommentService commentService;

    CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @Operation( summary = "Add comment to a post", description = "This endpoint is used to add comments to a post",
            tags = {"INTERACTIONS"})
    @PostMapping("/")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService
                .addComment(userPrincipal.getUserId(), postId, message), HttpStatus.CREATED);
    }

    @Operation( summary = "View a comment", description = "This endpoint is used to view a comment",
            tags = {"INTERACTIONS"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getComment(@PathVariable long id){
        return ResponseEntity.ok(this.commentService.getComment(id));
    }

    @Operation( summary = "Get all comments", description = "This endpoint fetches all the comments from a post",
            tags = {"INTERACTIONS"})
    @GetMapping("/")
    public ResponseEntity<?> getAllComments(@PathVariable long postId){
        return ResponseEntity.ok(this.commentService.getCommentsByPostId(postId));
    }

    @Operation( summary = "Update your comment", description = "User can update their own comments",
            tags = {"INTERACTIONS"})
    @PutMapping("/{Id}")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long Id, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService.updateComment(userPrincipal.getUserId(), Id, message), HttpStatus.ACCEPTED);
    }

    @Operation( summary = "Delete a comment", description = "Users can delete their own comments using this endpoint",
            tags = {"INTERACTIONS"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id){
        this.commentService.deleteComment(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
