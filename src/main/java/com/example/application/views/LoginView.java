package com.example.application.views;

import com.example.application.data.service.AuthService;
import com.example.application.data.service.AuthService.AuthException;
import com.example.application.views.RegisterView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Login")
@Route(value = "login")
@CssImport("./styles/views/login-view.css")
public class LoginView extends Div {
    
    public LoginView(AuthService authService) {
        setId("login-view");
        addClassName("login-view");
        
        
	    var username = new TextField("Username");
	    var password = new PasswordField("Password");
	    var loginBtn = new Button("Login", event -> {
	    	try {
				authService.authenticate(username.getValue(), password.getValue());
				UI.getCurrent().navigate("home");
			} catch (AuthException e) {
				Notification.show("Wrong credentials");
			}
	    });
	    loginBtn.addClickShortcut(Key.ENTER);
	    
	    add(
	    		new H1("CapMo"),
	    		username,
	    		password,
	    		loginBtn,
	    		new RouterLink("Register", RegisterView.class) // register link
	    );
    }

}
