package pl.aleh.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.aleh.models.Person;
import pl.aleh.service.PersonService;

@Component
public class PersonValidator implements Validator {

  private final PersonService personService;

  @Autowired
  public PersonValidator(PersonService peopleService) {
    this.personService = peopleService;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return Person.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    Person person = (Person) o;

    if (personService.getPersonByFullName(person.getFullName()).isPresent()) {
      errors.rejectValue("fullName", "", "A person with such a full name already exists");
    }
  }

}
