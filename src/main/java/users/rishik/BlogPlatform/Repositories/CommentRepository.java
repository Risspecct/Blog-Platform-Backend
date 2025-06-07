package users.rishik.BlogPlatform.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.rishik.BlogPlatform.Entities.Comment;
import users.rishik.BlogPlatform.Projections.CommentView;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentView> findByPostId(long postId);
    List<CommentView> findByUserId(long userId);
    CommentView findCommentById(long id);
}
