package seedu.fitchasers;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, Person> users;
    private Person currentUser;

    public UserManager() {
        this.users = new HashMap<>();
        // Initialize default user
        this.currentUser = new Person("DefaultUser");
        this.users.put("DefaultUser", currentUser);
    }

    // Get or create a user
    public Person getOrCreateUser(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        String normalizedName = name.trim();
        users.computeIfAbsent(normalizedName, k -> new Person(normalizedName));
        currentUser = users.get(normalizedName);
        return currentUser;
    }

    // Get current user
    public Person getCurrentUser() {
        return currentUser;
    }

    // Set current user
    public void setCurrentUser(String name) {
        currentUser = getOrCreateUser(name);
    }

    // Get all users
    public Map<String, Person> getUsers() {
        return users;
    }
}
