package pl.aleh.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.aleh.models.Book;
import pl.aleh.models.Person;
import pl.aleh.repositories.BookRepository;

@Component
public class BookService {

  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public List<Book> findAll(boolean sortByYear) {
    if (sortByYear) {
      return bookRepository.findAll(Sort.by("year"));
    } else {
      return bookRepository.findAll();
    }
  }

  public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
    if (sortByYear) {
      return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
    } else {
      return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }
  }

  public Book findOne(int id) {
    Optional<Book> foundBook = bookRepository.findById(id);
    return foundBook.orElse(null);
  }

  public List<Book> searchByTitle(String query) {
    return bookRepository.findByTitleStartingWith(query);
  }

  @Transactional
  public void save(Book book) {
    bookRepository.save(book);
  }

  @Transactional
  public void update(int id, Book updatedBook) {
    Book bookToBeUpdated = bookRepository.findById(id).get();

    updatedBook.setId(id);
    updatedBook.setOwner(bookToBeUpdated.getOwner()); // чтобы не терялась связь при обновлении

    bookRepository.save(updatedBook);
  }

  @Transactional
  public void delete(int id) {
    bookRepository.deleteById(id);
  }

  public Person getBookOwner(int id) {
    // Здесь Hibernate.initialize() не нужен, так как владелец (сторона One) загружается не лениво
    return bookRepository.findById(id).map(Book::getOwner).orElse(null);
  }

  @Transactional
  public void release(int id) {
    bookRepository.findById(id).ifPresent(
        book -> {
          book.setOwner(null);
          book.setTakenAt(null);
        });
  }

  @Transactional
  public void assign(int id, Person selectedPerson) {
    bookRepository.findById(id).ifPresent(
        book -> {
          book.setOwner(selectedPerson);
          book.setTakenAt(new Date()); // текущее время
        }
    );
  }

}
