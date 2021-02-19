package gymman.auth;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The Authentication and Authorization service handles user login, logout
 * and its permissions.
 */
public interface AuthService {

    /**
     * Logs the user in using a username and plaintext password
     * 
     * @param username
     * @param password
     */
    void login(String username, String password);

    /**
     * Logs the user out if logged in
     */
    void logout();

    /**
     * Check if some user is logged in
     * 
     * @return true if the user is logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Get the logged in User
     * 
     * @return A User instance if logged in, null otherwise
     */
    User getLoggedInUser();
    
    /**
     * Get the logged in User's permissions
     * 
     * @return A Set of Permission instances, null otherwise
     */
    Set<Permission> getUserPermissions();

    /**
     * Checks if the user has some permission
     * @param permission
     * @return true if the user has the specified permission, false otherwise
     */
    boolean userHasPermission(Permission permission);

    /**
     * Checks if the user has some permission, specified by its ID
     * @param permission
     * @return true if the user has the specified permission, false otherwise
     */
    boolean userHasPermission(String permissionId);

    /**
     * Register a permission
     * @param permission
     */
    void registerPermission(Permission permission);

    /**
     * Get all registered Permission
     * @return the list of Permission
     */
    List<Permission> getRegisteredPermissions();

    /**
     * Register a callback to be run on successful login
     * @param handler
     */
    void addOnLoginHandler(Consumer<User> handler);

    /**
     * Register a callback to be run on successful logout
     * @param handler
     */
    void addOnLogoutHandler(Consumer<User> handler);
}