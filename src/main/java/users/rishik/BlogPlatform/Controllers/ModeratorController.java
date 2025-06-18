package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Repositories.CommentRepository;
import users.rishik.BlogPlatform.Repositories.PostRepository;


@RestController
@RequestMapping("/mod")
@PreAuthorize("hasRole('MOD')")
public class ModeratorController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    ModeratorController(CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Operation( summary = "Delete any post", description = "Mods can delete any post using this endpoint", tags = {"MOD"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/delete/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable long postId){
        this.postRepository.deleteById(postId);
        return ResponseEntity.ok("Post with id: " + postId + " deleted successfully");
    }

    @Operation( summary = "Delete any comment", description = "Mods can delete any comment using this endpoint", tags = {"MOD"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "403", description = "Not authorized to access this resource"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/delete/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId){
        if (this.commentRepository.existsById(commentId)){
            this.commentRepository.deleteById(commentId);
            return ResponseEntity.ok("Comment deleted successfully with id: " + commentId);
        }
        throw new NotFoundException("Comment not found with id: " + commentId);
    }
}
