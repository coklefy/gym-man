package gymman.common;

public class DuplicateEntityException extends RuntimeException {
	private static final long serialVersionUID = 270211175010120511L;

	public DuplicateEntityException(String newEntity, String existingEntity) {
		super(String.format("Tried to add '%s' but '%s' already exists", newEntity, existingEntity));
	}
}
