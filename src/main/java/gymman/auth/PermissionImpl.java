package gymman.auth;

public class PermissionImpl implements Permission {
	private final String name;
	private String description;

	public PermissionImpl(final String name, final String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof PermissionImpl)) {
			return false;
		}
		return this.name.equals(((PermissionImpl) other).getName());
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s: %s", this.name, this.description);
	}
}
