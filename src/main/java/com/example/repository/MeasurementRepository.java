package com.example.repository;

import com.example.model.Measurement;
import com.example.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    Measurement findByPerson(Person person);
}
