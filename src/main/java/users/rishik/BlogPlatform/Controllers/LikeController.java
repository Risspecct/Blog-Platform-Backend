package users.rishik.BlogPlatform.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Security.UserPrincipal;
import users.rishik.BlogPlatform.Services.LikeService;

@RestController
@RequestMapping("posts/{postId}")
@PreAuthorize("hasRole('Viewer')")
public class LikeController {
    private final LikeService likeService;

    LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    @PostMapping("/addLike")
    public ResponseEntity<?> like(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId){
        return new ResponseEntity<>(this.likeService.addLike(userPrincipal.getUserId(), postId), HttpStatus.CREATED);
    }

    @GetMapping("/likes")
    public ResponseEntity<?> countLikes(@PathVariable long postId){
        return ResponseEntity.ok(this.likeService.getLikesByPost(postId));
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<?> removeLike(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long postId) {
        this.likeService.removeLike(userPrincipal.getUserId(), postId);
        return ResponseEntity.ok("Removed Like");
    }
}
