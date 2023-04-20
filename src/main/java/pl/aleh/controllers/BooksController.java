package pl.aleh.controllers;

import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.aleh.service.BookService;
import pl.aleh.service.PersonService;
import pl.aleh.models.Book;
import pl.aleh.models.Person;

@Controller
@RequestMapping("/books")
public class BooksController {

  private final BookService bookService;
  private final PersonService personService;

  @Autowired
  public BooksController(BookService bookService, PersonService personService) {
    this.bookService = bookService;
    this.personService = personService;
  }

  @GetMapping()
  public String index(Model model) {
    model.addAttribute("books", bookService.index());
    return "books/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
    model.addAttribute("book", bookService.show(id));

    Optional<Person> bookOwner = bookService.getBookOwner(id);

    if (bookOwner.isPresent()) {
      model.addAttribute("owner", bookOwner.get());
    } else {
      model.addAttribute("people", personService.index());
    }

    return "books/show";
  }

  @GetMapping("/new")
  public String newBook(@ModelAttribute("book") Book Book) {
    return "books/new";
  }

  @PostMapping()
  public String create(
      @ModelAttribute("book") @Valid Book Book,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return "books/new";
    }

    bookService.save(Book);
    return "redirect:/books";
  }

  @GetMapping("/{id}/edit")
  public String edit(Model model, @PathVariable("id") int id) {
    model.addAttribute("book", bookService.show(id));
    return "books/edit";
  }

  @PatchMapping("/{id}")
  public String update(
      @ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
      @PathVariable("id") int id
  ) {
    if (bindingResult.hasErrors()) {
      return "books/edit";
    }

    bookService.update(id, book);
    return "redirect:/books";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    bookService.delete(id);
    return "redirect:/books";
  }

  @PatchMapping("/{id}/release")
  public String release(@PathVariable("id") int id) {
    bookService.release(id);
    return "redirect:/books/" + id;
  }

  @PatchMapping("/{id}/assign")
  public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
    bookService.assign(id, selectedPerson);
    return "redirect:/books/" + id;
  }

}
