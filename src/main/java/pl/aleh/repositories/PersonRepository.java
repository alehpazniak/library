package pl.aleh.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.aleh.models.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
  Optional<Person> findByFullName(String fullName);

}
