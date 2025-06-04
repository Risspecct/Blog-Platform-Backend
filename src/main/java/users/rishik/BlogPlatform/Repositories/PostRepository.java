package users.rishik.BlogPlatform.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.rishik.BlogPlatform.Entities.Post;
import users.rishik.BlogPlatform.Projections.PostView;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<PostView>> findAllByUserId(long userId);
    Optional<PostView> findPostById(long id);
}
