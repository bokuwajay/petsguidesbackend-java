package live.codeland.petsguidesbackend.repository;

import live.codeland.petsguidesbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

}
