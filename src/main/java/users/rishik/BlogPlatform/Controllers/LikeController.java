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
        try {
            return new ResponseEntity<>(this.likeService.addLike(userId, postId), HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/likes")
    public ResponseEntity<?> countLikes(@PathVariable long postId){
        try{
            return ResponseEntity.ok(this.likeService.getLikesByPost(postId));
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<?> removeLike(@PathVariable long userId, @PathVariable long postId){
        try{
            this.likeService.removeLike(userId, postId);
            return ResponseEntity.ok("Removed Like");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
