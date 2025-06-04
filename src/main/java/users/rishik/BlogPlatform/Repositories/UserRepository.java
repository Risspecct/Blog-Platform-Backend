package users.rishik.BlogPlatform.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Projections.UserView;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<UserView> findAllBy();

    Optional<UserView> findUserById(long id);
}
