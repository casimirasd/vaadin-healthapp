package com.example.application.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.html.Anchor;

@Route("about")
@PageTitle("About - Vaadin Health App")
public class AboutView extends VerticalLayout {

    public AboutView() {
        H1 title = new H1("About Us");
        add(title);

 
        add("Koulu vaadin harjoitus työ.");


        Anchor homeLink = new Anchor("/", "Back to Home");
        add(homeLink);
    }
}
