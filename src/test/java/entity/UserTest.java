package entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.application.data.entity.Profile;
import com.example.application.data.entity.User;

class UserTest {

    @Test
    void checkPasswordMatchesTest() {
        String password = "password";
        User user = new User("username", password, null, true);
        boolean result = user.checkPassword(password);

        assertTrue(result);
    }

    @Test
    void checkPasswordDoesNotMatchTest() {
        String password = "password";
        String wrongPassword = "wrongpassword";
        User user = new User("username", password, null, true);
        boolean result = user.checkPassword(wrongPassword);

        assertFalse(result);
    }

    @Test
    void addFollowingTest() {
        User user = new User("username", "password", null, true);
        Profile profile = new Profile();
        user.addFollowing(profile);

        assertTrue(user.getFollowing().contains(profile));
    }

    @Test
    void removeFollowingTest() {
        // Arrange
        User user = new User("username", "password", null, true);
        Profile profile = new Profile();
        user.addFollowing(profile);
        user.removeFollowing(profile);

        assertFalse(user.getFollowing().contains(profile));
    }

    @Test
    void isFollowing_True_Test() {
        User user = new User("username", "password", null, true);
        Profile profile = new Profile();
        user.addFollowing(profile);
        boolean result = user.isFollowing(profile);

        assertTrue(result);
    }

    @Test
    void isFollowing_False_Test() {
        User user = new User("username", "password", null, true);
        Profile profile = new Profile();
        boolean result = user.isFollowing(profile);

        assertFalse(result);
    }
}