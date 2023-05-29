package com.example.application.views;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.example.application.data.service.AuthService;
import com.example.application.data.service.IUserRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
@CssImport("./styles/views/register-view.css")
public class RegisterView extends Composite {

    private final AuthService authService;
    private final IUserRepo userRepo;

    public RegisterView(AuthService authService, IUserRepo userRepo) {
        setId("register-view");
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @Override
    protected Component initContent() {
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        DatePicker birthDate = new DatePicker("Birth Date");
        birthDate.setMax(LocalDate.now().minusYears(13));
        return new VerticalLayout(
                new H2("Register"),
                username,
                password1,
                password2,
                birthDate,
                new Button("Register", event -> register(
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue(),
                        birthDate.getValue()
                )),
                new Button("Back", event ->
                        UI.getCurrent().navigate(LoginView.class))
        );
    }

    private void register(String username, String password1, String password2, LocalDate birthDate) {
    	if (username.trim().isEmpty()) {
            Notification.show("Enter a username!");
        } else if (username.contains(" ")) {
            Notification.show("Username cannot contain empty spaces!");
        } else if (username.length() < 4) {
            Notification.show("Username must be at least 4 characters long!");
//        } else if (password1.isEmpty()) {
//            Notification.show("Enter a password!");
//        } else if (password1.contains(" ")) {
//            Notification.show("Password cannot contain empty spaces!");
//        } else if (password1.length() < 8) {
//            Notification.show("Password must be at least 8 characters long!");
//        } else if (!password1.matches(".*\\d.*")) {
//            Notification.show("Password must contain at least one digit!");
//        } else if (!password1.matches(".*[A-Z].*")) {
//            Notification.show("Password must contain at least one uppercase letter!");
//        } else if (!password1.equals(password2)) {
//            Notification.show("Passwords don't match!");
//        } else if (ChronoUnit.YEARS.between(birthDate, LocalDate.now()) < 13) {
//            Notification.show("You must be 13 years or older to register!");
        } else {
            authService.register(username, password1);
        }
    }
}