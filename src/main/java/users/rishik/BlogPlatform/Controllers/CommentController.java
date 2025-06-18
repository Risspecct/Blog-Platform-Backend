package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "403", description = "Not logged in"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService
                .addComment(userPrincipal.getUserId(), postId, message), HttpStatus.CREATED);
    }

    @Operation( summary = "View a comment", description = "This endpoint is used to view a comment",
            tags = {"INTERACTIONS"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment retrieved"),
            @ApiResponse(responseCode = "403", description = "Not logged in"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getComment(@PathVariable long id){
        return ResponseEntity.ok(this.commentService.getComment(id));
    }

    @Operation( summary = "Get all comments", description = "This endpoint fetches all the comments from a post",
            tags = {"INTERACTIONS"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments Retrieved"),
            @ApiResponse(responseCode = "403", description = "Not logged in"),
            @ApiResponse(responseCode = "404", description = "Comments not found")
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllComments(@PathVariable long postId){
        return ResponseEntity.ok(this.commentService.getCommentsByPostId(postId));
    }

    @Operation( summary = "Update your comment", description = "User can update their own comments",
            tags = {"INTERACTIONS"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comment updated"),
            @ApiResponse(responseCode = "400", description = "Invalid Input"),
            @ApiResponse(responseCode = "403", description = "Invalid Credentials"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/{Id}")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long Id, @RequestBody String message){
        message = message.trim().replaceAll("^\"|\"$", "");
        return new ResponseEntity<>(this.commentService.updateComment(userPrincipal.getUserId(), Id, message), HttpStatus.ACCEPTED);
    }

    @Operation( summary = "Delete a comment", description = "Users can delete their own comments using this endpoint",
            tags = {"INTERACTIONS"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id){
        this.commentService.deleteComment(userPrincipal.getUserId(), id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
