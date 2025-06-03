package users.rishik.BlogPlatform.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.rishik.BlogPlatform.Entities.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostId(long postId);
    Optional<List<Comment>> findByUserId(long userId);
    Optional<List<Comment>> findAllByUserIdAndPostId(long userId, long postId);
}
