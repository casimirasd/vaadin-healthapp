package com.example.service;

import com.example.model.Person;
import com.example.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public Person getPersonWithAddresses(Long personId) {
        // Load person and ensure addresses are fetched
        Person person = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        // Trigger the loading of addresses (avoid LazyInitializationException)
        person.getAddresses().size();
        return person;
    }
}
