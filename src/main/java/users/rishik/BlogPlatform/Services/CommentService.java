package users.rishik.BlogPlatform.Services;

import org.springframework.stereotype.Service;
import users.rishik.BlogPlatform.Entities.Comment;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Exceptions.NotFoundException;
import users.rishik.BlogPlatform.Exceptions.UnauthorizedException;
import users.rishik.BlogPlatform.Repositories.CommentRepository;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    CommentService(CommentRepository commentRepository, UserService userService, PostService postService){
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public Comment addComment(long userId, long postId, String message){
        User user = this.userService.getUser(userId);
        Post post = this.postService.getPost(postId);
        if (message == null) throw new InvalidParameterException("Comment cant be blank");
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setComment(message);
        return this.commentRepository.save(comment);
    }

    public Comment getComment(long id){
        return this.commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not available"));
    }

    public List<Comment> getCommentsByPostId(long postId){
        return this.commentRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException("No Comments for this post"));
    }

    public List<Comment> getCommentsByUserId(long userId){
        return this.commentRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No comments found by user"));
    }

    public Comment updateComment(long userId, long id, String message){
        Comment comment = this.getComment(id);
        if (comment.getUser().getId() != userId)
            throw new UnauthorizedException("You are not allowed to edit this comment");
        comment.setComment(message);
        return this.commentRepository.save(comment);
    }

    public void deleteComment(long userId, long id){
        Comment comment = this.getComment(id);
        if (comment.getUser().getId() != userId)
            throw new UnauthorizedException("You are not allowed to edit this comment");
        this.commentRepository.deleteById(id);
    }
}
