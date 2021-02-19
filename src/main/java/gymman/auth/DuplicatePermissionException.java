package gymman.auth;

public class DuplicatePermissionException extends Exception {
    private static final long serialVersionUID = 8143032355197686731L;

    public DuplicatePermissionException() {
        super();
    }

    public DuplicatePermissionException(String permissionName) {
        super(String.format("Permission '%s' already registered", permissionName));
    }
}