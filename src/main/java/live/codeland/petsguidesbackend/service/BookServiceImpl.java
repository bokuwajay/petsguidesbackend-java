package live.codeland.petsguidesbackend.service;

import live.codeland.petsguidesbackend.model.Book;
import live.codeland.petsguidesbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends BaseServiceImpl<Book, String> implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        super(bookRepository);
        this.bookRepository = bookRepository;
    }

}
