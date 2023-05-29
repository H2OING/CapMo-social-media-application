package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.data.entity.Notification;
import com.example.application.data.entity.NotificationType;
import com.example.application.data.entity.Profile;

class NotificationTest {

    @Mock
    private Profile mockProfile;

    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notification = new Notification("Description", mockProfile, NotificationType.FOLLOW);
    }

    @Test
    void SetDescriptionTest() {
        assertEquals("Description", notification.getDescription());
    }

    @Test
    void SetProfileTest() {
        assertEquals(mockProfile, notification.getProfile());
    }

    @Test
    void SetNotificationTypeTest() {
        assertEquals(NotificationType.FOLLOW, notification.getNotifType());
    }
}
