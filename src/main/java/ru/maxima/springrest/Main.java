package ru.maxima.springrest;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.maxima.springrest.test_model.Person;

public class Main {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://reqres.in/api/users";

//        Map<String, String> json = new HashMap<>();
//        json.put("name", "Trash");
//        json.put("job", "Software Engineer");
//
//        HttpEntity<Map<String, String>> request  = new HttpEntity<>(json);

        Person person  = Person.builder()
                .name("Trash")
                .job("Software Engineer")
                .build();

        HttpEntity<Person> request  = new HttpEntity<>(person);

        ResponseEntity<Person> response = restTemplate.postForEntity(url, request, Person.class);

        System.out.println(response);

//        RestEntity response = restTemplate.getForObject(url, RestEntity.class);
//
//        System.out.println(responce);

    }
}
