package gymman.auth;

import java.util.Optional;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;

public class UserRepositoryImpl extends MemoryRepository<User> implements UserRepository {
	@Override
	public void add(User user) throws DuplicateEntityException {
		Optional<User> existing = this.items.stream()
			.filter(e -> e.getUsername().equals(user.getUsername()) && !e.getId().equals(user.getId()))
			.findFirst();
		if (existing.isPresent()) {
			throw new DuplicateEntityException(user.toString(), existing.get().toString());
		}

		super.add(user);
	}

	@Override
	public Optional<User> getByUsername(String username) {
		return this.items.stream()
			.filter(e -> e.getUsername().equals(username))
			.findFirst();
	}

	@Override
	public boolean hasUsername(String username) {
		return this.getByUsername(username).isPresent();
	}
}
