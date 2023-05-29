package com.example.application.views.admin;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.UserPost;
import com.example.application.data.service.IProfileRepo;
import com.example.application.data.service.IProfileService;
import com.example.application.data.service.IUserPostRepo;
import com.example.application.data.service.IUserPostService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;


@PageTitle("Admin-Post")
@Route(value = "posts/:upID?/:action?(edit)", layout = MainLayout.class)
public class UserPostAdminView extends Div implements BeforeEnterObserver  {


    private UserPost post;
    private IUserPostRepo postRepo;  
    private IUserPostService postService;
    
	private final String USERPOST_ID = "upID";
    private final String USERPOST_EDIT_ROUTE_TEMPLATE = "posts/%s/edit";

    private final Grid<UserPost> grid = new Grid<>(UserPost.class, false);

    private TextField description;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<UserPost> binder;

    

    public UserPostAdminView(IUserPostService postService) {
        this.postService = postService;
        addClassNames("admin-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        grid.addColumn(new ComponentRenderer<>(post -> {
            Image postImage = new Image(new StreamResource("profile-picture.jpg", () -> new ByteArrayInputStream(post.getImage())), "Post Picture");
            postImage.setWidth("50px");
            postImage.setHeight("50px");
            return postImage;
        })).setHeader("Profile Picture").setAutoWidth(true);
        grid.addColumn(UserPost::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(UserPost::getCreatedDate).setHeader("Created").setAutoWidth(true);
        grid.addColumn(UserPost::likes).setHeader("Likes").setAutoWidth(true);
        grid.addColumn(UserPost::commentCount).setHeader("Comments").setAutoWidth(true);
        grid.addComponentColumn(post -> {
            Button button = new Button("", new Icon(VaadinIcon.SEARCH));
            button.addClickListener(e -> navigateToProfile(post.getProfile().getUser().getUsername()));
            return button;
        }).setAutoWidth(true);
        
        //TODO Finish
        
        grid.setItems(query -> postService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(USERPOST_EDIT_ROUTE_TEMPLATE, event.getValue().getUpID()));
            } else {
                clearForm();
                UI.getCurrent().navigate(UserPostAdminView.class);
            }
        });

        binder = new BeanValidationBinder<>(UserPost.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.post == null) {
                    this.post = new UserPost();
                }
                binder.writeBean(this.post);
                postService.update(this.post);
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
        Optional<Long> userPostId = event.getRouteParameters().get(USERPOST_ID).map(Long::parseLong);
        if (userPostId.isPresent()) {
            Optional<UserPost> samplePersonFromBackend = postService.get(userPostId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %s", userPostId.get()), 3000,
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
        description = new TextField("Description");
        formLayout.add(description);

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

    private void populateForm(UserPost value) {
        this.post = value;
        binder.readBean(this.post);

    }
    
    private void navigateToProfile(String username) {
        String url = "profiles/" + username;
        UI.getCurrent().getPage().setLocation(url);
    }
}
