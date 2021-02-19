package gymman.tool;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;


/**
 * The Class ToolRepository.
 */
public final class ToolRepository extends MemoryRepository<Tool> {

    /**
     * Control insert to Duplicate the same Tool with name and NumSerial equals
     * @param Tool the tool
     *
     */
    @Override
    public void add(final Tool tool) throws DuplicateEntityException {
        final Optional<Tool> existing = this.items.stream()
            .filter(e -> e.getName().equals(tool.getName()) && e.getNumseriale().equals(tool.getNumseriale()) && !e.getId().equals(tool.getId()))
            .findFirst();
        if (existing.isPresent()) {
            throw new DuplicateEntityException(tool.toString(), existing.get().toString());
        }
        super.add(tool);
    }

    /**
     * Gets the by name.
     *
     * @param name the name
     * @return the by name
     */
    public Optional<Tool> getByName(final String name) {
        return this.items.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();
    }

    /**
     * Search by name.
     *
     * @param name the name
     * @return the list
     */
    public List<Tool> searchByName(final String name) {
        return this.items.stream()
            .filter(e -> e.getName().contains(name))
            .collect(Collectors.toList());
    }
}
