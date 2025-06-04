package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import users.rishik.BlogPlatform.Entities.Like;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Projections.LikeView;
import users.rishik.BlogPlatform.Repositories.LikeRepository;
import users.rishik.BlogPlatform.Repositories.PostRepository;
import users.rishik.BlogPlatform.Repositories.UserRepository;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository){
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public LikeView addLike(long userId, long postId){
        if (this.likeRepository.existsByUserIdAndPostId(userId, postId))
            throw new IllegalStateException("Already liked the Post");
        User user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id:" + userId));
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found with id:" + postId));
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        this.likeRepository.save(like);
        return this.likeRepository.findLikeById(like.getId()).orElseThrow(() -> new NotFoundException("Like unavailable"));
    }

    public long getLikesByPost(long postId){
        return this.likeRepository.countByPostId(postId);
    }

    @Transactional
    public void removeLike(long userId, long postId){
        this.likeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
