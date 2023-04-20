package pl.aleh.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pl.aleh.models.Book;
import pl.aleh.models.Person;

@Component
public class BookService {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public BookService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Book> index() {
    return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
  }

  public Book show(int id) {
    return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), new Object[]{id})
        .stream().findFirst().orElse(null);
  }

  public void save(Book book) {
    jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES(?, ?, ?)", book.getTitle(),
        book.getAuthor(), book.getYear());
  }

  public void update(int id, Book book) {
    jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?", book.getTitle(), book.getAuthor(),
        book.getYear(), id);
  }

  public void delete(int id) {
    jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
  }

  public Optional<Person> getBookOwner(int id) {
    return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.id WHERE Book.id = ?",
        new BeanPropertyRowMapper<>(Person.class), new Object[]{id}).stream().findFirst();
  }

  public void release(int id) {
    jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
  }

  public void assign(int id, Person person) {
    jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", person.getId(), id);
  }

}
