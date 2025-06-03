package users.rishik.BlogPlatform.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import users.rishik.BlogPlatform.Entities.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(long postId);
    void deleteByUserIdAndPostId(long userId, long postId);
    boolean existsByUserIdAndPostId(long userId, long postId);
}
