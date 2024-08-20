// Let's break down this line:
List<Book> books = bookService.getAllBooks();

// First, let's define what the Book class might look like:
public class Book {
    private Long id;
    private String title;
    private String author;

    // Constructor, getters, and setters omitted for brevity
}

// Now, let's imagine what the BookService class might look like:
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

// The BookRepository interface might look like this:
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // No need to define findAll() as it's provided by JpaRepository
}

// Now, let's walk through what happens when bookService.getAllBooks() is called:

// 1. The controller calls bookService.getAllBooks()
List<Book> books = bookService.getAllBooks();

// 2. Inside BookService, this calls bookRepository.findAll()
public List<Book> getAllBooks() {
    return bookRepository.findAll();
}

// 3. JpaRepository's findAll() method queries the database and returns all Book entities

// 4. The result is a List<Book> containing all books in the database

// Example of what might be returned:
List<Book> books = [
    new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald"),
    new Book(2L, "To Kill a Mockingbird", "Harper Lee"),
    new Book(3L, "1984", "George Orwell")
];

// This list is then returned to the controller and eventually serialized to JSON
