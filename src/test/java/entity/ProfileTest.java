package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.data.entity.Notification;
import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPost;

class ProfileTest {

    @Mock
    private User mockUser;

    private Profile profile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        profile = new Profile(false, mockUser, "John", "Doe", "Bio");
    }

    @Test
    void addPostTest() {
        UserPost mockPost = mock(UserPost.class);
        profile.addPost(mockPost);

        Collection<UserPost> posts = profile.getPost();
        assertTrue(posts.contains(mockPost));
    }

    @Test
    void addFollowerAbdUserFollowingTest() {
        User mockFollower = mock(User.class);
        profile.addFollower(mockFollower);

        Set<User> followers = profile.getFollowers();
        assertTrue(followers.contains(mockFollower));

        verify(mockFollower).addFollowing(profile);
    }

    @Test
    void RemoveFollowerAndFollowingTest() {
        User mockFollower = mock(User.class);
        profile.addFollower(mockFollower);

        profile.removeFollower(mockFollower);

        Set<User> followers = profile.getFollowers();
        assertFalse(followers.contains(mockFollower));

        verify(mockFollower).removeFollowing(profile);
    }

    @Test
    void getUserPostCountTest() {
        UserPost mockPost1 = mock(UserPost.class);
        UserPost mockPost2 = mock(UserPost.class);

        Collection<UserPost> posts = new ArrayList<>();
        posts.add(mockPost1);
        posts.add(mockPost2);
        profile.setPost(posts);

        int postCount = profile.getUserPostCount();
        assertEquals(2, postCount);
    }

    @Test
    void getFollowersCountTest() {
        User mockFollower1 = mock(User.class);
        User mockFollower2 = mock(User.class);

        Set<User> followers = new HashSet<>();
        followers.add(mockFollower1);
        followers.add(mockFollower2);
        profile.setFollowers(followers);

        int followersCount = profile.getFollowersCount();
        assertEquals(2, followersCount);
    }

    @Test
    void getNotificationCountTest() {
        Notification mockNotification1 = mock(Notification.class);
        Notification mockNotification2 = mock(Notification.class);

        Collection<Notification> notifications = new ArrayList<>();
        notifications.add(mockNotification1);
        notifications.add(mockNotification2);
        profile.setNotification(notifications);

        int notificationCount = profile.getNotificationCount();
        assertEquals(2, notificationCount);
    }
}