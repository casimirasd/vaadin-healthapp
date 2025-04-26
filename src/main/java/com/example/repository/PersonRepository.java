package com.example.repository;

import com.example.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    // Custom query to find a person by first and last name
    Person findByFirstNameAndLastName(String firstName, String lastName);
}
