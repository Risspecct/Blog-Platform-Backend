package users.rishik.BlogPlatform.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.rishik.BlogPlatform.Services.LikeService;

@RestController
@RequestMapping("/users/{userId}/posts/{postId}")
public class LikeController {
    private final LikeService likeService;

    LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    @PostMapping("/addLike")
    public ResponseEntity<?> like(@PathVariable long userId, @PathVariable long postId){
        return new ResponseEntity<>(this.likeService.addLike(userId, postId), HttpStatus.CREATED);
    }

    @GetMapping("/likes")
    public ResponseEntity<?> countLikes(@PathVariable long postId){
        return ResponseEntity.ok(this.likeService.getLikesByPost(postId));
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<?> removeLike(@PathVariable long userId, @PathVariable long postId) {
        this.likeService.removeLike(userId, postId);
        return ResponseEntity.ok("Removed Like");
    }
}
