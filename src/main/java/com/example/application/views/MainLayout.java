package com.example.application.views;

import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.data.entity.Feedback;
import com.example.application.data.entity.FeedbackType;
import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.IFeedbackService;
import com.example.application.data.service.IUserService;
//import com.example.application.views.admin.AdminView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Arrays;

import org.vaadin.lineawesome.LineAwesomeIcon;

@CssImport("./styles/views/main-layout.css")
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    AuthService authService;
    
    IFeedbackService feedbackService;
    
    IUserService userService;
    
    public MainLayout(AuthService authService, IFeedbackService feedbackService, IUserService userService) {
    	this.authService = authService;
    	this.feedbackService = feedbackService;
    	this.userService = userService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();      
        addBottomRightButton();
    }

    private void addBottomRightButton() {
    	Button openDialogButton = new Button();
        openDialogButton.getStyle().set("position", "fixed");
        openDialogButton.getStyle().set("bottom", "20px");
        openDialogButton.getStyle().set("right", "20px");
        openDialogButton.getStyle().set("width", "70px");
        openDialogButton.getStyle().set("height", "70px");
        openDialogButton.getStyle().set("border-radius", "50%");
        
        Icon icon = new Icon(VaadinIcon.COMMENT_ELLIPSIS);
        icon.setSize("50px");
        openDialogButton.setIcon(icon);
        openDialogButton.addClickListener(e -> openDialog());

        getElement().appendChild(openDialogButton.getElement());
    }

    private void openDialog() {
    	Dialog dialog = new Dialog();
        dialog.setWidth("300px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        H2 dialogTitle = new H2("Feedback");
        dialogLayout.add(dialogTitle);

        TextArea messageField = new TextArea("Message");
        messageField.setRequired(true);
        ComboBox<FeedbackType> typeComboBox = new ComboBox<>("Feedback Type", Arrays.asList(FeedbackType.values()));
        typeComboBox.setRequired(true);

        Button submitButton = new Button("Submit", event -> {
            String message = messageField.getValue();
            FeedbackType type = typeComboBox.getValue();

            if (message != null && !message.isEmpty() && type != null) {
                Feedback feedback = new Feedback(message, type);                
                feedbackService.saveFeedback(feedback);
                dialog.close();
            } else {
            	Notification.show("Fill the fields!");
            }
        });

        dialogLayout.add(messageField, typeComboBox, submitButton);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("CapMo");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        AppNav nav = new AppNav();

        var user = VaadinSession.getCurrent().getAttribute(User.class);

        authService.getAuthorizedRoutes(user.getRole().getTitle()).stream()
        .map(r -> {
        	int notifCount = 0;
        	if(user.getRole().getTitle() == "USER") {
        		notifCount = user.getProfile().getNotification().size();
        	}
        	
            Icon icon = getIconForRoute(r.name(), notifCount);

            return new AppNavItem(r.name(), r.view(), icon);            
        })
        .forEach(nav::addItem);

        return nav;
    }

    private Icon getIconForRoute(String routeName, int notificationCount) {
        if (routeName.equals("Profile")) {
             return new Icon(VaadinIcon.USER); 
        } else if (routeName.equals("Home")){
            return new Icon(VaadinIcon.HOME);
        } else if (routeName.equals("Notificiation")){
        	Icon bellIcon = new Icon(VaadinIcon.BELL);
            if (notificationCount > 0) {
            	 bellIcon.addClassName("notification-icon");
                 bellIcon.getElement().setAttribute("badge", String.valueOf(notificationCount));
            }
            return bellIcon;
        } else if (routeName.equals("Message")){
            return new Icon(VaadinIcon.CHAT);
        } else if (routeName.equals("Search")){
            return new Icon(VaadinIcon.SEARCH);
        } else if (routeName.equals("Logout")){
            return new Icon(VaadinIcon.SIGN_OUT);
        } else if (routeName.equals("Admin-Profile")){
            return new Icon(VaadinIcon.USERS);
        } else if (routeName.equals("Admin-Post")){
            return new Icon(VaadinIcon.PICTURE);
        } else {
        	 return new Icon(VaadinIcon.HOME);
        }
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
                      