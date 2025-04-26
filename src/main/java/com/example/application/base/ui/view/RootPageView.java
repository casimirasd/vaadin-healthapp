package com.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("")
@AnonymousAllowed
public class RootPageView extends VerticalLayout {

    public RootPageView() {
        // Set up the page title and filler text
        H1 title = new H1("Welcome to the Application");
        title.getStyle().set("text-align", "center");

        // Filler text
        String fillerText = "This is the home page. Please log in to continue using the application.";
        H1 filler = new H1(fillerText);
        filler.getStyle().set("text-align", "center");

        // Create a button to navigate to the login page
        Button loginButton = new Button("Go to Login Page", event -> getUI().ifPresent(ui -> ui.navigate("login")));

        // Layout configuration
        setAlignItems(Alignment.CENTER);
        add(title, filler, loginButton);
    }
}
