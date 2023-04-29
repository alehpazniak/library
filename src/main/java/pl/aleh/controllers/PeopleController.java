package pl.aleh.controllers;

import jakarta.validation.Valid;
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
import pl.aleh.models.Person;
import pl.aleh.service.PersonService;
import pl.aleh.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {

  private final PersonService personService;
  private final PersonValidator personValidator;

  @Autowired
  public PeopleController(PersonService personService, PersonValidator personValidator) {
    this.personService = personService;
    this.personValidator = personValidator;
  }

  @GetMapping()
  public String index(Model model) {
    model.addAttribute("people", personService.findAll());
    return "people/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable("id") int id, Model model) {
    model.addAttribute("person", personService.findOne(id));
    model.addAttribute("books", personService.getBooksByPersonId(id));

    return "people/show";
  }

  @GetMapping("/new")
  public String newPerson(@ModelAttribute("person") Person person) {
    return "people/new";
  }

  @PostMapping()
  public String create(
      @ModelAttribute("person") @Valid Person person,
      BindingResult bindingResult
  ) {
    personValidator.validate(person, bindingResult);

    if (bindingResult.hasErrors()) {
      return "people/new";
    }

    personService.save(person);
    return "redirect:/people";
  }

  @GetMapping("/{id}/edit")
  public String edit(Model model, @PathVariable("id") int id) {
    model.addAttribute("person", personService.findOne(id));
    return "people/edit";
  }

  @PatchMapping("/{id}")
  public String update(
      @ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
      @PathVariable("id") int id
  ) {
    if (bindingResult.hasErrors()) {
      return "people/edit";
    }

    personService.update(id, person);
    return "redirect:/people";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    personService.delete(id);
    return "redirect:/people";
  }
}
