package com.example;

import com.example.model.Address;
import com.example.model.Measurement;
import com.example.model.Person;
import com.example.repository.AddressRepository;
import com.example.repository.MeasurementRepository;
import com.example.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired private PersonRepository personRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private MeasurementRepository measurementRepository;

    @PostConstruct
    public void init() {
        Person[] people = new Person[8];

        people[0] = createPerson("John", "Doe", 30);
        people[1] = createPerson("Alice", "Smith", 25);
        people[2] = createPerson("Bob", "Brown", 40);
        people[3] = createPerson("Clara", "Jones", 28);
        people[4] = createPerson("David", "Miller", 35);
        people[5] = createPerson("Ella", "Wilson", 22);
        people[6] = createPerson("Frank", "Anderson", 45);
        people[7] = createPerson("Grace", "Taylor", 33);

        for (Person p : people) {
            personRepository.save(p);
        }

        createAddress("Main St", "Helsinki", "00100", people[0]);
        createAddress("Broadway", "New York", "10001", people[1]);
        createAddress("King St", "London", "SW1A", people[2]);
        createAddress("River Rd", "Tampere", "33100", people[3]);
        createAddress("Lakeview", "Helsinki", "00150", people[4]);
        createAddress("Forest Ave", "Tampere", "33200", people[5]);
        createAddress("Hilltop Dr", "Oulu", "90100", people[6]);
        createAddress("Sunset Blvd", "Oulu", "90200", people[7]);

        createMeasurement(120, 70, 180, people[0]);
        createMeasurement(110, 65, 170, people[1]);
        createMeasurement(125, 80, 175, people[2]);
        createMeasurement(118, 60, 165, people[3]);
        createMeasurement(122, 75, 182, people[4]);
        createMeasurement(115, 68, 178, people[5]);
    }

    private Person createPerson(String first, String last, int age) {
        Person p = new Person();
        p.setFirstName(first);
        p.setLastName(last);
        p.setAge(age);
        return p;
    }

    private void createAddress(String street, String city, String postal, Person person) {
        Address a = new Address();
        a.setStreet(street);
        a.setCity(city);
        a.setPostalCode(postal);
        a.setPerson(person);
        addressRepository.save(a);
    }

    private void createMeasurement(double bp, double weight, double height, Person person) {
        Measurement m = new Measurement();
        m.setBloodPressure(bp);
        m.setWeight(weight);
        m.setHeight(height);
        m.setPerson(person);
        measurementRepository.save(m);
    }
}
