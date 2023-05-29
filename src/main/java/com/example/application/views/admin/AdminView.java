package com.example.application.views.admin;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.SamplePerson;
import com.example.application.data.entity.User;
import com.example.application.data.service.IProfileRepo;
import com.example.application.data.service.IProfileService;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Admin-Profile")
@Route(value = "admin/:pID?/:action?(edit)", layout = MainLayout.class)
@CssImport("./themes/capmo/views/admin-view.css")
public class AdminView extends Div implements BeforeEnterObserver {

    private Profile profile;
    private IProfileRepo profileRepo;  
    private IProfileService profileService;
    
	private final String PROFILE_ID = "pID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "admin/%s/edit";

    private final Grid<Profile> grid = new Grid<>(Profile.class, false);

    private TextField name;
    private TextField surname;
    private TextField bio;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Profile> binder;

    

    public AdminView(IProfileService profileService) {
        this.profileService = profileService;
        addClassNames("admin-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        grid.addColumn(new ComponentRenderer<>(profile -> {
            Image profileImage = new Image(new StreamResource("profile-picture.jpg", () -> new ByteArrayInputStream(profile.getProfilePicture())), "Profile Picture");
            profileImage.setWidth("50px");
            profileImage.setHeight("50px");
            return profileImage;
        })).setHeader("Profile Picture").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("surname").setAutoWidth(true);
        grid.addColumn("bio").setAutoWidth(true);
        grid.addColumn(Profile::getUserPostCount).setHeader("User Posts").setAutoWidth(true);
        grid.addColumn(Profile::getNotificationCount).setHeader("User Notifications").setAutoWidth(true);
        grid.addColumn(Profile::getFollowersCount).setHeader("Followers").setAutoWidth(true);
        grid.addColumn(Profile::getFollowingCount).setHeader("Following").setAutoWidth(true);
        grid.addComponentColumn(profile -> {
            Button button = new Button("", new Icon(VaadinIcon.SEARCH));
            button.addClickListener(e -> navigateToProfile(profile.getUser().getUsername()));
            return button;
        }).setAutoWidth(true);
        
        grid.setItems(query -> profileService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getPID()));
            } else {
                clearForm();
                UI.getCurrent().navigate(AdminView.class);
            }
        });

        binder = new BeanValidationBinder<>(Profile.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.profile == null) {
                    this.profile = new Profile();
                }
                binder.writeBean(this.profile);
                profileService.update(this.profile);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(AdminView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> samplePersonId = event.getRouteParameters().get(PROFILE_ID).map(Long::parseLong);
        if (samplePersonId.isPresent()) {
            Optional<Profile> samplePersonFromBackend = profileService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %s", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(AdminView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("First Name");
        surname = new TextField("Last Name");
        bio = new TextField("Bio");
        formLayout.add(name, surname, bio);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Profile value) {
        this.profile = value;
        binder.readBean(this.profile);

    }
    
    private void navigateToProfile(String username) {
        String url = "profiles/" + username;
        UI.getCurrent().getPage().setLocation(url);
    }
}
