package ru.maxima.springrest.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springrest.dto.AuthDTO;
import ru.maxima.springrest.dto.PersonDTO;
import ru.maxima.springrest.models.Person;
import ru.maxima.springrest.security.PersonDetails;
import ru.maxima.springrest.service.PeopleService;
import ru.maxima.springrest.util.JWTUtil;
import ru.maxima.springrest.validation.PersonValidator;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final PeopleService peopleService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final PersonValidator personValidator;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(PeopleService peopleService, JWTUtil jwtUtil, ModelMapper modelMapper, PersonValidator personValidator, AuthenticationManager authenticationManager) {
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.personValidator = personValidator;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(authDTO.getName(), authDTO.getPassword());

        try {
            authenticationManager.authenticate(userToken);
        } catch (AuthenticationException e) {
            return Map.of("error", "incorrect login or password");
        }

        String token  = jwtUtil.generateToken(authDTO.getName());

        return Map.of("jwt-token", token);
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody @Valid PersonDTO personDTO,
                            BindingResult bindingResult) {

        log.debug("Received registration request: {}", personDTO);

        Person person = peopleService.convertFromDTOToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return Map.of("message", "error body");
        }

        peopleService.save(person);

        String token = jwtUtil.generateToken(person.getName());

        return Map.of("jwt-token", token);
    }

    @GetMapping("/show")
    public String showAuthenticatedUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails principal = (PersonDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

}
