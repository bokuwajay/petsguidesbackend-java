package live.codeland.petsguidesbackend.controller;

import jakarta.validation.Valid;
import live.codeland.petsguidesbackend.dto.PaginationDto;
import live.codeland.petsguidesbackend.dto.ApiResponseDto;
import live.codeland.petsguidesbackend.model.Book;
import live.codeland.petsguidesbackend.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BookController extends BaseController<Book, String>{
    private final BookService bookService;

    public BookController(BookService bookService) {
        super(bookService);
        this.bookService = bookService;
    }

    // batch create
    @PostMapping("/batch-creation")
    public ResponseEntity<ApiResponseDto<List<Book>>> createBooks(@Valid @RequestBody List<Book> books) {
        LocalDateTime now = LocalDateTime.now();

        // Set default values for each book
        books.forEach(book -> {
            if (book.getTitle() == null) book.setTitle("Untitled Book");
            if (book.getAuthor() == null) book.setAuthor("Unknown Author");
            book.setPublished(true);
            book.setCreatedAt(now);
            book.setUpdatedAt(now);
        });

        List<Book> savedBooks = super.saveAll(books); // Make sure your superclass has this method

        ApiResponseDto<List<Book>> response = new ApiResponseDto<>(
                HttpStatus.OK,
                200,
                savedBooks,
                "Books created successfully!",
                now
        );

        return response.toClient();
    }



    // create one
    @PostMapping("/creation")
    public ResponseEntity<ApiResponseDto<Book>> createBook(@Valid @RequestBody Book book) {
        Book savedBook = super.save(book);

        ApiResponseDto<Book> response = new ApiResponseDto<>(
                HttpStatus.OK,
                200,
                savedBook,
                "Book created successfully!",
                LocalDateTime.now()
        );

        return response.toClient();
    }


    // get all with pagination
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDto<PaginationDto<Book>>> getAllBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy) {

        return super.findAll(page, limit, sortBy, orderBy);

    }

    // get one by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Optional<Book>>> getOneById(@PathVariable("id") String id) {
        return super.findById(id);
    }


    // update 1 by id
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Book>> updateBook(@RequestBody Book book, @PathVariable("id") String id){
        return super.updateOne(book,id);
    }

    // soft delete batch
    @DeleteMapping("/batch-deletion")
    public ResponseEntity<ApiResponseDto<List<Book>>> softDeleteBookList(@RequestBody List<String> idList){
        return super.softDeleteAll(idList);
    }

    //  soft delete 1 by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Book>> softDeleteBookById(@PathVariable String id) {

        return super.softDeleteOne(id);
    }

}