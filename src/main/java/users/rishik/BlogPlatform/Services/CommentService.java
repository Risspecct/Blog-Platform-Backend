package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Entities.Comment;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Exceptions.UnauthorizedException;
import users.rishik.BlogPlatform.Projections.CommentView;
import users.rishik.BlogPlatform.Repositories.PostRepository;
import users.rishik.BlogPlatform.Repositories.UserRepository;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class CommentService {
    private final users.rishik.BlogPlatform.Repositories.CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    CommentService(users.rishik.BlogPlatform.Repositories.CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public CommentView addComment(long userId, long postId, String message){
        User user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id:" + userId));
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found with id:" + postId));
        if (message == null) throw new InvalidParameterException("Comment cant be blank");
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setComment(message);
        this.commentRepository.save(comment);
        return this.commentRepository.findCommentById(comment.getId());
    }

    public CommentView getComment(long id){
        this.commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not available"));
        return this.commentRepository.findCommentById(id);
    }

    public List<CommentView> getCommentsByPostId(long postId){
        return this.commentRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException("No Comments for this post"));
    }

    public List<CommentView> getCommentsByUserId(long userId){
        return this.commentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No comments found by user"));
    }

    public CommentView updateComment(long userId, long id, String message){
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment unavailable"));
        if (comment.getUser().getId() != userId)
            throw new UnauthorizedException("You are not allowed to edit this comment");
        comment.setComment(message);
        this.commentRepository.save(comment);
        return this.commentRepository.findCommentById(id);
    }

    public void deleteComment(long userId, long id){
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment unavailable"));
        if (comment.getUser().getId() != userId)
            throw new UnauthorizedException("You are not allowed to edit this comment");
        this.commentRepository.deleteById(id);
    }
}
