package users.rishik.BlogPlatform.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.rishik.BlogPlatform.Entities.Like;
import users.rishik.BlogPlatform.Projections.LikeView;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(long postId);
    void deleteByUserIdAndPostId(long userId, long postId);
    boolean existsByUserIdAndPostId(long userId, long postId);
    Optional<LikeView> findLikeById(long id);
}
