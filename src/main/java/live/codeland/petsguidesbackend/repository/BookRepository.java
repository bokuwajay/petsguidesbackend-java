package live.codeland.petsguidesbackend.repository;
import live.codeland.petsguidesbackend.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BookRepository extends MongoRepository<Book, String> { }

