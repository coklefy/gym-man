package gymman.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;

	private Optional<User> user;
	private boolean loggedIn = false;
	private final Set<Permission> permissions = new HashSet<>();
	private List<Consumer<User>> onLoginHandlers = new ArrayList<>();
	private List<Consumer<User>> onLogoutHandlers = new ArrayList<>();

	public AuthServiceImpl(final UserRepository userRepo, final RoleRepository roleRepo) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
	}

	@Override
	public void login(final String username, final String password) {
		Optional<User> user = this.userRepo.getByUsername(username);
		if (!user.isPresent()) {
			throw new LoginException(String.format("User '%s' not found", username));
		}

		if (!user.get().verifyPassword(password)) {
			throw new LoginException(String.format("Invalid password for user '%s'", username));
		}

		this.user = user;
		this.loggedIn = true;

		for (Consumer<User> handler : this.onLoginHandlers) {
			handler.accept(user.get());
		}
	}

	@Override
	public void logout() {
		if (!this.isLoggedIn()) {
			return;
		}
		User tmp = this.user.get();
		this.user = Optional.empty();
		this.loggedIn = false;

		for (Consumer<User> handler : this.onLogoutHandlers) {
			handler.accept(tmp);
		}
	}

	@Override
	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	@Override
	public User getLoggedInUser() {
		if (!this.loggedIn) {
			throw new NotLoggedInException();
		}
		return this.user.get();
	}

	@Override
	public Set<Permission> getUserPermissions() {
		if (!this.loggedIn) {
			return new HashSet<>();
		}

		Optional<Role> role = this.roleRepo.getByName(this.user.get().getRole());

		if (!role.isPresent()) {
			return new HashSet<>();
		}

		return role.get().getPermissions();

	}

	@Override
	public boolean userHasPermission(final Permission permission) {
		if (!this.isLoggedIn()) {
			return false;
		}

		return this.getUserPermissions().contains(permission);
	}

	@Override
	public boolean userHasPermission(String permissionId) {
		if (!this.isLoggedIn()) {
			return false;
		}

		return this.getUserPermissions().stream()
			.filter(e -> e.getName().equals(permissionId))
			.findFirst()
			.isPresent();
	}

	@Override
	public void registerPermission(Permission permission) {
		this.permissions.add(permission);
	}

	@Override
	public List<Permission> getRegisteredPermissions() {
		return new ArrayList<>(this.permissions);
	}

	@Override
	public void addOnLoginHandler(Consumer<User> handler) {
		this.onLoginHandlers.add(handler);
	}

	@Override
	public void addOnLogoutHandler(Consumer<User> handler) {
		this.onLogoutHandlers.add(handler);
	}
}
