package ru.maxima.springrest.controllers;

//CRUD

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springrest.dto.PersonDTO;
import ru.maxima.springrest.exceptions.PersonErrorResponse;
import ru.maxima.springrest.exceptions.PersonNotCreatedException;
import ru.maxima.springrest.exceptions.PersonNotFoundException;
import ru.maxima.springrest.models.Person;
import ru.maxima.springrest.service.PeopleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController{

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<PersonDTO> getAllPeople(){

        List<Person> allPeople = peopleService.getAllPeople();
        List<PersonDTO> personDTOList  = new ArrayList<>();

        for  (Person person : allPeople) {
            personDTOList.add(peopleService.convertFromPersonToPersonDTO(person));
        }

        return personDTOList;  //Jackson сконвентирует объекты в JSON
    }

    @GetMapping("/{id}")
    public PersonDTO getPersonById(@PathVariable("id") Long id) {

        Person byId = peopleService.findById(id);

        return peopleService.convertFromPersonToPersonDTO(byId); //Jackson сконвентирует объекты в JSON
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> savePerson(@RequestBody @Valid PersonDTO personDTO,
                             BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();

            List<FieldError> fieldErrors = result.getFieldErrors();

            for (FieldError fieldError : fieldErrors)  {
                errors.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(";");
            }

            throw new PersonNotCreatedException(errors.toString());
        }

        Person person = peopleService.convertFromDTOToPerson(personDTO);

        peopleService.save(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    //ExceptionHandler
    @ExceptionHandler({PersonNotFoundException.class})
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException ex) {
        PersonErrorResponse response = new PersonErrorResponse(
                ex.getMessage(), new Date()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PersonNotCreatedException.class})
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException ex) {
        PersonErrorResponse response = new PersonErrorResponse(
                ex.getMessage(), new Date()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
