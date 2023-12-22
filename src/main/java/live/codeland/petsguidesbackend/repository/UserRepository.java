package live.codeland.petsguidesbackend.repository;

import live.codeland.petsguidesbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {


}
