package com.example.application.views;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import com.example.application.data.entity.Notification;
import com.example.application.data.entity.NotificationType;
import com.example.application.data.entity.Profile;
import com.example.application.data.service.INotificationService;
import com.example.application.data.service.IProfileService;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;

@PageTitle("Notification")
@CssImport("./styles/views/notification-view.css")
public class NotificationView extends VerticalLayout {
    
    private Grid<Notification> notificationGrid = new Grid<>(Notification.class);
    
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM HH:mm");
    
    public NotificationView(INotificationService notifService, IProfileService profileService) {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        
        Profile currentProfile = profileService.getCurrentProfile();
        
        notificationGrid.removeAllColumns();
        notificationGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        notificationGrid.setHeightFull();
        
        notificationGrid.addColumn(new ComponentRenderer<>(notification -> {
            if (notification.getNotifType() == NotificationType.LIKE) {
                Icon heartIcon = VaadinIcon.HEART.create();
                heartIcon.setColor("red");
                return new Span(heartIcon);
            } else if (notification.getNotifType() == NotificationType.FOLLOW) {
            	Icon followIcon = VaadinIcon.USER_CHECK.create();
                return new Span(followIcon);
            } else if (notification.getNotifType() == NotificationType.COMMENT) {
            	Icon followIcon = VaadinIcon.CHAT.create();
                return new Span(followIcon);
            } else {
                return new Span();
            }
        })).setWidth("100px").setFlexGrow(0);
        
        notificationGrid.addColumn(Notification::getDescription)
        	.setAutoWidth(true);
        	
        notificationGrid.addColumn(notification -> notification.getCreatedDate().format(dateFormatter))
        	.setWidth("150px")
        	.setTextAlign(ColumnTextAlign.END)
        	.setAutoWidth(true);
        
        List<Notification> notifications = notifService.getAllNotifications(currentProfile);
        notifications.sort(Comparator.comparing(Notification::getCreatedDate).reversed());
        notificationGrid.setItems(notifications);
        
        add(notificationGrid);
    }
}





