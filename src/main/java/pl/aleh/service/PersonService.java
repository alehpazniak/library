package pl.aleh.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.aleh.models.Book;
import pl.aleh.models.Person;
import pl.aleh.repositories.PersonRepository;

@Component
public class PersonService {

  private final PersonRepository personRepository;

  @Autowired
  public PersonService(PersonRepository peopleRepository) {
    this.personRepository = peopleRepository;
  }

  public List<Person> findAll() {
    return personRepository.findAll();
  }

  public Person findOne(int id) {
    Optional<Person> foundPerson = personRepository.findById(id);
    return foundPerson.orElse(null);
  }

  @Transactional
  public void save(Person person) {
    personRepository.save(person);
  }

  @Transactional
  public void update(int id, Person updatedPerson) {
    updatedPerson.setId(id);
    personRepository.save(updatedPerson);
  }

  @Transactional
  public void delete(int id) {
    personRepository.deleteById(id);
  }

  public Optional<Person> getPersonByFullName(String fullName) {
    return personRepository.findByFullName(fullName);
  }

  public List<Book> getBooksByPersonId(int id) {
    Optional<Person> person = personRepository.findById(id);

    if (person.isPresent()) {
      Hibernate.initialize(person.get().getBooks());
      person.get().getBooks().forEach(book -> {
        long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
        if (diffInMillies > 864000000) {
          book.setExpired(true);
        }
      });

      return person.get().getBooks();
    } else {
      return Collections.emptyList();
    }
  }

}
