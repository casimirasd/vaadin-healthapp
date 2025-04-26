package com.example.application.base.ui.view;

import com.example.model.Address;
import com.example.model.Measurement;
import com.example.model.Person;
import com.example.repository.AddressRepository;
import com.example.repository.MeasurementRepository;
import com.example.repository.PersonRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.access.annotation.Secured;


import java.util.List;
import java.util.stream.Collectors;

@Route("main")
@Component
@RolesAllowed("ADMIN")
@PageTitle("Vaadin Health App")
public class MainView extends VerticalLayout {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final MeasurementRepository measurementRepository;

    private final TextField streetField = new TextField("Street");
    private final TextField cityField = new TextField("City");
    private final TextField postalCodeField = new TextField("Postal Code");
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("Last Name");
    private final Button saveButton = new Button("Save Person");

    private final TextField nameFilterField = new TextField("Filter by Name");
    private final TextField cityFilterField = new TextField("Filter by City");
    private final TextField postalCodeFilterField = new TextField("Filter by Postal Code");

    private final Grid<Person> personGrid = new Grid<>(Person.class);

    private final Dialog infoDialog = new Dialog();
    private final Dialog measurementDialog = new Dialog();

    private List<Person> allPersons; // store all persons for filtering

    @Autowired
    public MainView(AddressRepository addressRepository, PersonRepository personRepository, MeasurementRepository measurementRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.measurementRepository = measurementRepository;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        Div header = createHeader();
        Div navBar = createNavigationBar();
        add(header, navBar);

        Div mainContent = createMainContent();
        Scroller scroller = new Scroller(mainContent);
        scroller.setSizeFull();
        add(scroller);
        expand(scroller);

        Div footer = createFooter();
        add(footer);

        loadPersons();
    }

    private Div createHeader() {
        return new Div(); // can be extended later
    }

    private Div createNavigationBar() {
        Div navBar = new Div();
        navBar.addClassName("custom-nav-bar");
        navBar.setWidthFull();

        Div navContent = new Div();
        navContent.addClassName("custom-nav-content");

        H1 title = new H1("Vaadin Health App");
        title.addClassName("custom-nav-title");

        Tabs tabs = new Tabs(
            new Tab("Home"),
            new Tab("About"),
            new Tab("Contact")
        );

        tabs.addSelectedChangeListener(event -> {
            // Tab switching logic if needed
        });

        navContent.add(title, tabs);
        navBar.add(navContent);
        return navBar;
    }

    private Div createFooter() {
        Div footer = new Div();
        footer.addClassName("footer");
        footer.add("© 2025 My SPA App. All rights reserved.");
        return footer;
    }

    private Div createMainContent() {
        Div mainContent = new Div();
        mainContent.addClassName("main-content");
        mainContent.setWidthFull();

        FormLayout formLayout = new FormLayout(
            streetField, cityField, postalCodeField, firstNameField, lastNameField, saveButton
        );

        saveButton.addClickListener(event -> saveAddress());

        // Filters
        nameFilterField.setPlaceholder("First or Last Name");
        cityFilterField.setPlaceholder("City");
        postalCodeFilterField.setPlaceholder("Postal Code");

        nameFilterField.addValueChangeListener(e -> applyFilters());
        cityFilterField.addValueChangeListener(e -> applyFilters());
        postalCodeFilterField.addValueChangeListener(e -> applyFilters());

        Div filters = new Div(nameFilterField, cityFilterField, postalCodeFilterField);
        filters.addClassName("filter-layout");

        personGrid.removeAllColumns();
        personGrid.addColumn(Person::getFirstName).setHeader("First Name");
        personGrid.addColumn(Person::getLastName).setHeader("Last Name");
        personGrid.addColumn(Person::getAge).setHeader("Age");

        personGrid.addColumn(person -> {
            Address addr = person.getAddresses().isEmpty() ? null : person.getAddresses().get(0);
            return addr != null ? addr.getStreet() : "";
        }).setHeader("Street");

        personGrid.addColumn(person -> {
            Address addr = person.getAddresses().isEmpty() ? null : person.getAddresses().get(0);
            return addr != null ? addr.getCity() : "";
        }).setHeader("City");

        personGrid.addColumn(person -> {
            Address addr = person.getAddresses().isEmpty() ? null : person.getAddresses().get(0);
            return addr != null ? addr.getPostalCode() : "";
        }).setHeader("Postal Code");

        personGrid.addComponentColumn(person -> {
            Button edit = new Button("Edit");
            edit.addClickListener(e -> openPersonalInfoDialog(person));
            return edit;
        });

        personGrid.addComponentColumn(person -> {
            Button measurements = new Button("Measurements");
            measurements.addClickListener(e -> openMeasurementDialog(person));
            return measurements;
        });

        personGrid.setWidthFull();

        mainContent.add(formLayout, filters, personGrid);
        return mainContent;
    }

    private void loadPersons() {
        allPersons = personRepository.findAll();
        personGrid.setItems(allPersons);
    }

    private void applyFilters() {
        String name = nameFilterField.getValue().trim().toLowerCase();
        String city = cityFilterField.getValue().trim().toLowerCase();
        String postalCode = postalCodeFilterField.getValue().trim().toLowerCase();

        List<Person> filtered = allPersons.stream()
            .filter(person -> {
                boolean nameMatch = name.isEmpty() ||
                        (person.getFirstName() != null && person.getFirstName().toLowerCase().contains(name)) ||
                        (person.getLastName() != null && person.getLastName().toLowerCase().contains(name));

                boolean cityMatch = city.isEmpty() ||
                        (person.getAddresses() != null && person.getAddresses().stream()
                            .anyMatch(addr -> addr.getCity() != null && addr.getCity().toLowerCase().contains(city)));

                boolean postalCodeMatch = postalCode.isEmpty() ||
                        (person.getAddresses() != null && person.getAddresses().stream()
                            .anyMatch(addr -> addr.getPostalCode() != null && addr.getPostalCode().toLowerCase().contains(postalCode)));

                return nameMatch && cityMatch && postalCodeMatch;
            })
            .collect(Collectors.toList());

        personGrid.setItems(filtered);
    }

    private void saveAddress() {
        String firstName = firstNameField.getValue();
        String lastName = lastNameField.getValue();

        Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);
        if (person == null) {
            person = new Person();
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setAge(30); // default age
            person = personRepository.save(person);
        }

        Address address = new Address();
        address.setStreet(streetField.getValue());
        address.setCity(cityField.getValue());
        address.setPostalCode(postalCodeField.getValue());
        address.setPerson(person);

        addressRepository.save(address);

        Notification.show("Address saved");
        loadPersons();
    }

    private void openPersonalInfoDialog(Person person) {
        infoDialog.removeAll();

        TextField firstName = new TextField("First Name", person.getFirstName());
        TextField lastName = new TextField("Last Name", person.getLastName());
        TextField age = new TextField("Age", String.valueOf(person.getAge()));

        Button save = new Button("Save");
        save.addClickListener(event -> {
            try {
                person.setFirstName(firstName.getValue());
                person.setLastName(lastName.getValue());
                person.setAge(Integer.parseInt(age.getValue()));
                personRepository.save(person);
                Notification.show("Person updated");
                loadPersons();
                infoDialog.close();
            } catch (NumberFormatException e) {
                Notification.show("Invalid age format");
            }
        });

        infoDialog.add(firstName, lastName, age, save);
        infoDialog.open();
    }

    private void openMeasurementDialog(Person person) {
        measurementDialog.removeAll();

        Measurement measurement = measurementRepository.findByPerson(person);
        final Measurement currentMeasurement = (measurement != null) ? measurement : new Measurement();

        TextField bloodPressure = new TextField("Blood Pressure",
                measurement != null ? String.valueOf(measurement.getBloodPressure()) : "");
        TextField weight = new TextField("Weight",
                measurement != null ? String.valueOf(measurement.getWeight()) : "");
        TextField height = new TextField("Height",
                measurement != null ? String.valueOf(measurement.getHeight()) : "");

        Button save = new Button("Save");
        save.addClickListener(event -> {
            try {
                currentMeasurement.setPerson(person);
                currentMeasurement.setBloodPressure(Double.parseDouble(bloodPressure.getValue()));
                currentMeasurement.setWeight(Double.parseDouble(weight.getValue()));
                currentMeasurement.setHeight(Double.parseDouble(height.getValue()));
                measurementRepository.save(currentMeasurement);
                Notification.show("Measurement saved");
                measurementDialog.close();
            } catch (NumberFormatException e) {
                Notification.show("Invalid input in measurements");
            }
        });

        measurementDialog.add(bloodPressure, weight, height, save);
        measurementDialog.open();
    }
}
