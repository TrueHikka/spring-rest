package ru.maxima.springrest.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maxima.springrest.dto.PersonDTO;
import ru.maxima.springrest.exceptions.PersonNotFoundException;
import ru.maxima.springrest.models.Person;
import ru.maxima.springrest.repositories.PeopleRepository;
import ru.maxima.springrest.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final ModelMapper mapper;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, ModelMapper mapper, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<Person> getAllPeople() {
        String token = jwtUtil.generateToken("Vitaliy_Polyamory");

        String validateToken = jwtUtil.validateTokenAndReturnUsername(token);

        System.out.println(validateToken);

        return peopleRepository.findAll();
    }

    @Transactional
    public Person findById(Long id) {
        Optional<Person> byId = peopleRepository.findById(id);

        return byId.orElseThrow(() -> new PersonNotFoundException("Person with this id is not found"));
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person person, Long id) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void softDeletePerson(Long id) {
        Person person = peopleRepository.findById(id).orElseThrow();
        person.setRemoved(true);
        person.setRemovedAt(LocalDateTime.now());
        peopleRepository.save(person);
    }

    @Transactional
    public List<Person> getDeletedPeople() {
        return peopleRepository.findByRemovedTrue();
    }

    public PersonDTO convertFromPersonToPersonDTO(Person person) {
        //Указываем первым аргументом, из какой сущности будем делать
        //Вторым - тот класс, к кот. хотим привести
        return mapper.map(person, PersonDTO.class);

        //Заместо этого используем библиотеку ModelMapper
//        PersonDTO personDTO = new PersonDTO();
//        personDTO.setAge(byId.getAge());
//        personDTO.setName(byId.getName());
//        personDTO.setEmail(byId.getEmail());
//        return personDTO;
    }

    public Person convertFromDTOToPerson(PersonDTO personDTO) {

        Person person = mapper.map(personDTO, Person.class);

        enrichPerson(person);

        return person;

//        Person person = new Person();
//        person.setAge(personDTO.getAge());
//        person.setName(personDTO.getName());
//        person.setEmail(personDTO.getEmail());
//
//        enrichPerson(person);
//
//        return person;
    }

    private void enrichPerson(Person person)  {
        person.setCreatedAt(LocalDateTime.now());
        person.setCreatedBy("ADMIN");
        person.setRole("ROLE_USER");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRemoved(false);
    }
}
