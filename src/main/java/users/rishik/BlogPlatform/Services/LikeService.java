package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import users.rishik.BlogPlatform.Entities.Like;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Repositories.LikeRepository;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    LikeService(LikeRepository likeRepository, UserService userService, PostService postService){
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public Like addLike(long userId, long postId){
        if (this.likeRepository.existsByUserIdAndPostId(userId, postId))
            throw new IllegalStateException("Already liked the Post");
        User user = this.userService.getUser(userId);
        Post post = this.postService.getPost(postId);
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        return this.likeRepository.save(like);
    }

    public long getLikesByPost(long postId){
        return this.likeRepository.countByPostId(postId);
    }

    @Transactional
    public void removeLike(long userId, long postId){
        this.likeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
