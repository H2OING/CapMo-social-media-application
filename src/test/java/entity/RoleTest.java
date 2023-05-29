package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;

class RoleTest {

    @Test
    void addUserAccount_ShouldAddUserToUsersList() {
        Role role = new Role("Admin", "Administrator role");
        User user = new User();
        role.addUserAccount(user);

        assertTrue(role.getUsers().contains(user));
    }

    @Test
    void deleteUserAccount_ShouldRemoveUserFromUsersList() {
        Role role = new Role("Admin", "Administrator role");
        User user = new User();
        role.addUserAccount(user);
        role.deleteUserAccount(user);

        assertFalse(role.getUsers().contains(user));
    }

    @Test
    void constructor_ShouldSetTitleAndDesc() {
        String title = "Admin";
        String desc = "Administrator role";
        Role role = new Role(title, desc);

        assertEquals(title, role.getTitle());
        assertEquals(desc, role.getDesc());
    }
}