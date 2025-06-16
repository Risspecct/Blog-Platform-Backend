package users.rishik.BlogPlatform.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.LikeService;

@RestController
@RequestMapping("posts/{postId}")
@PreAuthorize("hasRole('VIEWER')")
public class LikeController {
    private final LikeService likeService;

    LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    @Operation( summary = "Like a post", description = "This endpoint is used to like a post", tags = {"INTERACTIONS"})
    @PostMapping("/addLike")
    public ResponseEntity<?> like(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId){
        return new ResponseEntity<>(this.likeService.addLike(userPrincipal.getUserId(), postId), HttpStatus.CREATED);
    }

    @Operation( summary = "Get all Likes", description = "This endpoint gets the count of all the likes on a post",
            tags = {"INTERACTIONS"})
    @GetMapping("/likes")
    public ResponseEntity<?> countLikes(@PathVariable long postId){
        return ResponseEntity.ok(this.likeService.getLikesByPost(postId));
    }

    @Operation( summary = "Remove a like", description = "This endpoint removes a like from a specified post",
            tags = {"INTERACTIONS"})
    @DeleteMapping("/unlike")
    public ResponseEntity<?> removeLike(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId) {
        this.likeService.removeLike(userPrincipal.getUserId(), postId);
        return ResponseEntity.ok("Removed Like");
    }
}
